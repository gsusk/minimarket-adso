package org.adso.minimarket.service;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import lombok.extern.slf4j.Slf4j;
import org.adso.minimarket.dto.SearchFilters;
import org.adso.minimarket.dto.SearchResult;
import org.adso.minimarket.models.document.ProductDocument;
import org.adso.minimarket.repository.elastic.ProductSearchRepository;
import org.adso.minimarket.validation.AttributeSchemaValidator;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Set;

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
        List<String> brands = extractBrand(searchHits);

//        Set<String> facetableAttributes = attributeSchemaValidator.getFacetableAttributes(filters.getCategory());
//        Map<String, List<String>> facets = extractFacets(searchHits, facetableAttributes);

        return SearchResult.builder()
                .products(products)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .brands(brands)
//              .facets(facets)
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
                                )
                        )
                        .should(s -> s.prefix(p -> p
                                        .field("name")
                                        .value(query.toLowerCase())
                                )
                        )
                )
        );

//        if (filters.getBrand() != null && !filters.getBrand().trim().isEmpty()) {
//            bool.filter(f -> f.term(t -> t.field("brand").value(filters.getBrand()).caseInsensitive(true)));
//
//        }

        if (filters.getCategory() != null && !filters.getCategory().trim().isEmpty()) {
            bool.filter(f -> f.term(t -> t.field("category").value(filters.getCategory()).caseInsensitive(true)));
        }

//        if (filters.getAttributes() != null && !filters.getAttributes().isEmpty()) {
//            filters.getAttributes().forEach((key, value) -> {
//                if (value != null && !value.trim().isEmpty()) {
//                    bool.filter(f -> f.term(t -> t.field("specifications." + key + ".keyword").value(value)
//                    .caseInsensitive(true)));
//                }
//            });
//        }

        BoolQuery.Builder postFilter = new BoolQuery.Builder();
        boolean priceFilter = false;

        if (isValidMinPrice(filters)) {
            postFilter.filter(f -> f.range(r -> r.number(n -> n
                    .field("price").gte(filters.getMinPrice().doubleValue()))));
            priceFilter = true;
        }

        if (isValidMaxPrice(filters)) {
            postFilter.filter(f -> f.range(r -> r.number(n -> n
                    .field("price").lte(filters.getMaxPrice().doubleValue()))));
            priceFilter = true;
        }

        NativeQueryBuilder nq = NativeQuery.builder()
                .withQuery(q -> q.bool(bool.build()))
                .withAggregation("min_price", AggregationBuilders.min(m -> m.field("price")))
                .withAggregation("max_price", AggregationBuilders.max(m -> m.field("price")))
                .withAggregation("brands", AggregationBuilders.terms(t -> t
                        .field("brand")
                        .size(6)
                ))
                .withMaxResults(20);

        if (filters.getBrand() != null) {
            nq.withFilter(f -> f.term(t -> t.field("brand").value(filters.getBrand())));
        }
        if (priceFilter) {
            nq.withFilter(f -> f.bool(postFilter.build()));
        }

        return nq.build();
    }

    private List<String> extractBrand(SearchHits<ProductDocument> searchHits) {
        try {
            ElasticsearchAggregations agg = (ElasticsearchAggregations) searchHits.getAggregations();
            Aggregate aggregate = agg.get("brands").aggregation().getAggregate();

            if (aggregate.isMissing()) {
                return List.of();
            }

            return aggregate.sterms().buckets().array().stream().map(b -> b.key().stringValue() + b.docCount()).toList();
        } catch (Exception e) {
            log.error("Elastic search brand agg error: {} : {}", e.getCause(), e.getMessage());
            log.error("Stack: {}", e.getStackTrace());
            return List.of();
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

//    private Map<String, List<String>> extractFacets(SearchHits<ProductDocument> searchHits, Set<String> attributes) {
//        Map<String, List<String>> facets = new HashMap<>();
//        try {
//            ElasticsearchAggregations agg = (ElasticsearchAggregations) searchHits.getAggregations();
//            if (agg == null) return facets;
//
//            for (String attr : attributes) {
//                if (agg.get(attr) != null) {
//                    Aggregate aggregate = agg.get(attr).aggregation().getAggregate();
//
//                    if (aggregate.isSterms()) {
//                        List<String> values = aggregate.sterms().buckets().array().stream()
//                                .map(b -> b.key().stringValue())
//                                .toList();
//                        if (!values.isEmpty()) facets.put(attr, values);
//                    } else if (aggregate.isLterms()) {
//                        List<String> values = aggregate.lterms().buckets().array().stream()
//                                .map(b -> String.valueOf(b.key()))
//                                .toList();
//                        if (!values.isEmpty()) facets.put(attr, values);
//                    } else if (aggregate.isDterms()) {
//                        List<String> values = aggregate.dterms().buckets().array().stream()
//                                .map(b -> String.valueOf(b.key()))
//                                .toList();
//                        if (!values.isEmpty()) facets.put(attr, values);
//                    }
//                }
//            }
//        } catch (Exception e) {
//            log.warn("Failed to extract facets: {}", e.getMessage());
//        }
//        return facets;
//    }

    private void sanitizeFilters(SearchFilters filters) {
        if (filters.getAttributes() == null || filters.getAttributes().isEmpty()) {
            return;
        }

        Set<String> allowedAttributes = attributeSchemaValidator.getAllowedAttributes(filters.getCategory());
        filters.getAttributes().keySet().retainAll(allowedAttributes);

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

