package org.adso.minimarket.service;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.extern.slf4j.Slf4j;
import org.adso.minimarket.dto.SearchFilters;
import org.adso.minimarket.dto.SearchResult;
import org.adso.minimarket.models.document.ProductDocument;
import org.adso.minimarket.repository.elastic.ProductSearchRepository;
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

@Slf4j
@Service
public class SearchServiceImpl implements SearchService {
    private final ProductSearchRepository searchRepository;
    private final ElasticsearchOperations operations;

    public SearchServiceImpl(ProductSearchRepository searchRepository, ElasticsearchOperations operations) {
        this.searchRepository = searchRepository;
        this.operations = operations;
    }

    @Override
    public void saveIndex(ProductDocument product) {
        searchRepository.save(product);
    }

    @Override
    public SearchResult searchWithFilters(SearchFilters filters, String query) {
        NativeQuery searchQuery = buildQuery(filters, query);
        SearchHits<ProductDocument> searchHits = operations.search(searchQuery, ProductDocument.class);

        List<ProductDocument> products = searchHits.getSearchHits()
                .stream()
                .map(SearchHit::getContent)
                .toList();

        BigDecimal minPrice = extractMinPrice(searchHits);
        BigDecimal maxPrice = extractMaxPrice(searchHits);
        List<String> brands = extractBrand(searchHits);

        return SearchResult.builder()
                .products(products)
                .minPrice(minPrice)
                .maxPrice(maxPrice)
                .brands(brands)
                .total(searchHits.getSearchHits().size())
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

        if (filters.getBrand() != null && !filters.getBrand().trim().isEmpty()) {
            bool.filter(f -> f.term(t -> t.field("specifications.brand").value(filters.getBrand())));
        }

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
                .withQuery(q -> q.bool(bool.build())) // Afecta a aggs y resultados
                .withAggregation("min_price", AggregationBuilders.min(m -> m.field("price")))
                .withAggregation("max_price", AggregationBuilders.max(m -> m.field("price")))
                .withAggregation("brands", AggregationBuilders.terms(t -> t
                        .field("specifications.brand.keyword")
                        .size(50)
                ))
                .withMaxResults(20);

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

            return aggregate.sterms().buckets().array().stream().map(b -> b.key().stringValue()).toList();
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

    private boolean isValidMinPrice(SearchFilters filters) {
        return filters.getMinPrice() != null
                && filters.getMinPrice().compareTo(BigDecimal.ZERO) > 0;
    }

    private boolean isValidMaxPrice(SearchFilters filters) {
        return filters.getMaxPrice() != null
                && filters.getMaxPrice().compareTo(BigDecimal.ZERO) > 0;
    }
}

