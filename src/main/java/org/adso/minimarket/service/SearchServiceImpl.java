package org.adso.minimarket.service;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TextQueryType;
import lombok.extern.slf4j.Slf4j;
import org.adso.minimarket.dto.FacetValue;
import org.adso.minimarket.dto.SearchFilters;
import org.adso.minimarket.dto.SearchResult;
import org.adso.minimarket.models.document.ProductDocument;
import org.adso.minimarket.repository.elastic.ProductSearchRepository;
import org.adso.minimarket.validation.AttributeDefinition;
import org.adso.minimarket.validation.AttributeSchemaValidator;
import org.adso.minimarket.validation.FacetStrategy;
import org.adso.minimarket.validation.FilterType;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Slf4j
@Service
public class SearchServiceImpl implements SearchService {
    private final ProductSearchRepository searchRepository;
    private final ElasticsearchOperations operations;
    private final AttributeSchemaValidator attributeSchemaValidator;

    public SearchServiceImpl(ProductSearchRepository searchRepository, ElasticsearchOperations operations,
                             AttributeSchemaValidator attributeSchemaValidator) {
        this.searchRepository = searchRepository;
        this.operations = operations;
        this.attributeSchemaValidator = attributeSchemaValidator;
    }

    @Override
    public void saveIndex(ProductDocument product) {
        searchRepository.save(product);
    }

    @Override
    public SearchResult searchWithFilters(SearchFilters filters, String query) {
        sanitizeFilters(filters);
        NativeQuery searchQuery = buildQuery(filters, query);
        SearchHits<ProductDocument> searchHits = operations.search(searchQuery, ProductDocument.class);

        List<ProductDocument> products = searchHits.getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .toList();

        BigDecimal minPrice = extractMinPrice(searchHits);
        BigDecimal maxPrice = extractMaxPrice(searchHits);
        Map<String, List<FacetValue>> facets = extractFacets(searchHits, filters.getCategory());

        return SearchResult.builder()
                .products(products)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .facets(facets)
                .total(searchHits.getTotalHits())
                .build();
    }

    private NativeQuery buildQuery(SearchFilters filters, String query) {
        BoolQuery.Builder bool = new BoolQuery.Builder();

        bool.must(mu -> mu
                .bool(b -> b
                        .should(s -> s
                                .multiMatch(m -> m
                                        .fields("name^3", "description^1")
                                        .query(query)
                                        .fuzziness("AUTO")
                                        .type(TextQueryType.BestFields)
                                )
                        )
                        .should(s -> s.prefix(p -> p
                                        .field("name")
                                        .value(query.toLowerCase())
                                )
                        )
                )
        );

        if (filters.getCategory() != null && !filters.getCategory().trim().isEmpty()) {
            bool.filter(f -> f.term(t -> t.field("category").value(filters.getCategory()).caseInsensitive(true)));
        }

        BoolQuery.Builder postFilter = new BoolQuery.Builder();
        boolean hasPostFilters = false;

        if (isValidMinPrice(filters)) {
            postFilter.filter(f -> f.range(r -> r.number(n -> n
                    .field("price").gte(filters.getMinPrice().doubleValue()))));
            hasPostFilters = true;
        }

        if (isValidMaxPrice(filters)) {
            postFilter.filter(f -> f.range(r -> r.number(n -> n
                    .field("price").lte(filters.getMaxPrice().doubleValue()))));
            hasPostFilters = true;
        }

        if (filters.getBrand() != null && !filters.getBrand().trim().isEmpty()) {
            postFilter.filter(f -> f.term(t -> t.field("brand").value(filters.getBrand()).caseInsensitive(true)));
            hasPostFilters = true;
        }

        if (filters.getAttributes() != null && !filters.getAttributes().isEmpty()) {
            for (Map.Entry<String, String> entry : filters.getAttributes().entrySet()) {
                String attrName = entry.getKey();
                String attrValue = entry.getValue();

                if (attrValue == null || attrValue.trim().isEmpty()) {
                    continue;
                }

                FilterType filterType = attributeSchemaValidator.getFilterType(filters.getCategory(), attrName);
                applyAttributeFilter(postFilter, attrName, attrValue, filterType);
                hasPostFilters = true;
            }
        }

        NativeQueryBuilder nq = NativeQuery.builder()
                .withQuery(q -> q.bool(bool.build()))
                .withAggregation("min_price", AggregationBuilders.min(m -> m.field("price")))
                .withAggregation("max_price", AggregationBuilders.max(m -> m.field("price")))
                .withMaxResults(20);

        addFacetAggregations(nq, filters.getCategory());

        if (hasPostFilters) {
            nq.withFilter(f -> f.bool(postFilter.build()));
        }

        return nq.build();
    }

    private void applyAttributeFilter(BoolQuery.Builder postFilter, String attrName, String attrValue,
                                      FilterType filterType) {
        String fieldPath = "specifications." + attrName;

        switch (filterType) {
            case TERM -> postFilter.filter(f -> f.term(t -> t
                    .field(fieldPath + ".keyword")
                    .value(attrValue)
                    .caseInsensitive(true)));

            case MULTI_SELECT -> {
                String[] values = attrValue.split(",");
                if (values.length == 1) {
                    postFilter.filter(f -> f.term(t -> t
                            .field(fieldPath + ".keyword")
                            .value(values[0].trim())
                            .caseInsensitive(true)));
                } else {
                    postFilter.filter(f -> f.terms(t -> t
                            .field(fieldPath + ".keyword")
                            .terms(ts -> ts.value(Arrays.stream(values)
                                    .map(String::trim)
                                    .map(FieldValue::of)
                                    .toList()))));
                }
            }

            case BOOLEAN -> postFilter.filter(f -> f.term(t -> t
                    .field(fieldPath)
                    .value(Boolean.parseBoolean(attrValue))));

            case RANGE -> {
                try {
                    if (attrValue.contains("-")) {
                        String[] parts = attrValue.split("-");
                        double min = Double.parseDouble(parts[0].trim());
                        double max = Double.parseDouble(parts[1].trim());
                        postFilter.filter(f -> f.range(r -> r.number(n -> n
                                .field(fieldPath)
                                .gte(min)
                                .lte(max))));
                    } else {
                        double value = Double.parseDouble(attrValue.trim());
                        postFilter.filter(f -> f.term(t -> t
                                .field(fieldPath)
                                .value(value)));
                    }
                } catch (NumberFormatException e) {
                    log.warn("invalid range value for attribute '{}': {}", attrName, attrValue);
                }
            }

            default -> log.warn("attribute '{}' has filter type NONE, skipping filter", attrName);
        }
    }

    private void addFacetAggregations(NativeQueryBuilder nq, String category) {
        nq.withAggregation("brand", AggregationBuilders.terms(t -> t
                .field("brand")
                .size(20)));

        Map<String, AttributeDefinition> definitions = attributeSchemaValidator.getAttributeDefinitions(category);

        for (AttributeDefinition def : definitions.values()) {
            if (!def.isFacetable()) {
                continue;
            }

            String attrName = def.getName();
            FacetStrategy strategy = def.getFacetStrategy();
            String fieldPath = "specifications." + attrName + ".keyword";

            Aggregation agg = buildFacetAggregation(fieldPath, strategy);
            if (agg != null) {
                nq.withAggregation(attrName, agg);
            }
        }
    }

    private Aggregation buildFacetAggregation(String fieldPath, FacetStrategy strategy) {
        return switch (strategy) {
            case TERMS -> AggregationBuilders.terms(t -> t
                    .field(fieldPath)
                    .size(strategy.getDefaultSize()));

            case SIGNIFICANT_TERMS -> AggregationBuilders.significantTerms(st -> st
                    .field(fieldPath)
                    .size(strategy.getDefaultSize()));

            case SAMPLER -> AggregationBuilders.sampler(s -> s
                    .shardSize(strategy.getSampleSize()));

            case NONE -> null;
        };
    }

    private Map<String, List<FacetValue>> extractFacets(SearchHits<ProductDocument> searchHits, String category) {
        Map<String, List<FacetValue>> facets = new HashMap<>();

        try {
            ElasticsearchAggregations agg = (ElasticsearchAggregations) searchHits.getAggregations();
            if (agg == null) return facets;

            extractTermsFacet(agg, "brand", facets);

            Map<String, AttributeDefinition> definitions = attributeSchemaValidator.getAttributeDefinitions(category);

            for (AttributeDefinition def : definitions.values()) {
                if (!def.isFacetable()) {
                    continue;
                }

                String attrName = def.getName();
                FacetStrategy strategy = def.getFacetStrategy();

                switch (strategy) {
                    case TERMS, SIGNIFICANT_TERMS -> extractTermsFacet(agg, attrName, facets);
                    case SAMPLER -> extractSamplerFacet(agg, attrName, facets);
                    case NONE -> {
                    } // Skip
                }
            }
        } catch (Exception e) {
            log.warn("Failed to extract facets: {}", e.getMessage());
        }

        return facets;
    }

    private void extractTermsFacet(ElasticsearchAggregations agg, String facetName,
                                   Map<String, List<FacetValue>> facets) {
        try {
            if (agg.get(facetName) == null) {
                return;
            }

            Aggregate aggregate = agg.get(facetName).aggregation().getAggregate();

            if (aggregate.isSterms()) {
                List<FacetValue> values = aggregate.sterms().buckets().array().stream()
                        .map(b -> new FacetValue(b.key().stringValue(), b.docCount()))
                        .filter(fv -> fv.getCount() > 0)
                        .toList();
                if (!values.isEmpty()) {
                    facets.put(facetName, values);
                }
            } else if (aggregate.isLterms()) {
                List<FacetValue> values = aggregate.lterms().buckets().array().stream()
                        .map(b -> new FacetValue(String.valueOf(b.key()), b.docCount()))
                        .filter(fv -> fv.getCount() > 0)
                        .toList();
                if (!values.isEmpty()) {
                    facets.put(facetName, values);
                }
            } else if (aggregate.isDterms()) {
                List<FacetValue> values = aggregate.dterms().buckets().array().stream()
                        .map(b -> new FacetValue(String.valueOf(b.key()), b.docCount()))
                        .filter(fv -> fv.getCount() > 0)
                        .toList();
                if (!values.isEmpty()) {
                    facets.put(facetName, values);
                }
            } else if (aggregate.isSigsterms()) {
                List<FacetValue> values = aggregate.sigsterms().buckets().array().stream()
                        .map(b -> new FacetValue(b.key(), b.docCount()))
                        .filter(fv -> fv.getCount() > 0)
                        .toList();
                if (!values.isEmpty()) {
                    facets.put(facetName, values);
                }
            }
        } catch (Exception e) {
            log.debug("failed to extract terms facet '{}': {}", facetName, e.getMessage());
        }
    }

    private void extractSamplerFacet(ElasticsearchAggregations agg, String facetName,
                                     Map<String, List<FacetValue>> facets) {
        try {
            if (agg.get(facetName) == null) {
                return;
            }

            Aggregate samplerAgg = agg.get(facetName).aggregation().getAggregate();
            if (!samplerAgg.isSampler()) {
                return;
            }

            Map<String, Aggregate> subAggs = samplerAgg.sampler().aggregations();
            if (subAggs.containsKey("sampled_terms")) {
                Aggregate termsAgg = subAggs.get("sampled_terms");
                if (termsAgg.isSterms()) {
                    List<FacetValue> values = termsAgg.sterms().buckets().array().stream()
                            .map(b -> new FacetValue(b.key().stringValue(), b.docCount()))
                            .filter(fv -> fv.getCount() > 0)
                            .toList();
                    if (!values.isEmpty()) {
                        facets.put(facetName, values);
                    }
                }
            }
        } catch (Exception e) {
            log.debug("Failed to extract sampler facet '{}': {}", facetName, e.getMessage());
        }
    }

    private BigDecimal extractMinPrice(SearchHits<ProductDocument> searchHits) {
        return extractAggregationValue(searchHits, "min_price");
    }

    private BigDecimal extractMaxPrice(SearchHits<ProductDocument> searchHits) {
        return extractAggregationValue(searchHits, "max_price");
    }

    private BigDecimal extractAggregationValue(SearchHits<ProductDocument> searchHits, String aggName) {
        try {
            ElasticsearchAggregations aggs = (ElasticsearchAggregations) searchHits.getAggregations();
            Aggregate aggregate = aggs.get(aggName).aggregation().getAggregate();

            Double value = aggName.equals("min_price")
                    ? aggregate.min().value()
                    : aggregate.max().value();

            if (value == null || value.isNaN() || value.isInfinite()) {
                return null;
            }

            return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
        } catch (Exception e) {
            log.warn("Failed to extract aggregation '{}': {}", aggName, e.getMessage());
            return null;
        }
    }

    private void sanitizeFilters(SearchFilters filters) {
        if (filters.getAttributes() == null || filters.getAttributes().isEmpty()) {
            return;
        }

        Set<String> filterableAttributes = attributeSchemaValidator.getFilterableAttributes(filters.getCategory());
        filters.getAttributes().keySet().retainAll(filterableAttributes);

        if (filters.getAttributes().isEmpty()) {
            log.debug("All attribute filters were invalid for category: {}", filters.getCategory());
        }
    }

    private boolean isValidMinPrice(SearchFilters filters) {
        return filters.getMinPrice() != null
                && filters.getMinPrice().compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean isValidMaxPrice(SearchFilters filters) {
        return filters.getMaxPrice() != null
                && filters.getMaxPrice().compareTo(BigDecimal.ZERO) > 0;
    }
}
