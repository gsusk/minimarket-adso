package org.adso.minimarket.service;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import lombok.extern.slf4j.Slf4j;
import org.adso.minimarket.dto.SearchFilters;
import org.adso.minimarket.models.document.ProductDocument;
import org.adso.minimarket.repository.elastic.ProductSearchRepository;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    public List<ProductDocument> searchWithFilters(SearchFilters filters, String query) {
        BoolQuery.Builder bool = new BoolQuery.Builder();
        bool.must(mu -> mu
                .match(m -> m
                        .field("name")
                        .query(query)
                        .fuzziness("1")
                )
        );


        if (filters.getBrand() != null) {
            bool.filter(f -> f
                    .term(t -> t
                            .field("specifications.brand")
                            .value(filters.getBrand())
                    )
            );
        }

        if (filters.getMinPrice() != null && filters.getMinPrice().compareTo(BigDecimal.ZERO) > 0) {
            bool.filter(f -> f
                    .range(r -> r
                            .number(n -> n
                                    .field("price")
                                    .gte(filters.getMinPrice().doubleValue())
                            )
                    )
            );
        }

        if (filters.getMaxPrice() != null
                && filters.getMaxPrice().compareTo(BigDecimal.ZERO) > 0
                && filters.getMaxPrice().compareTo(filters.getMinPrice()) > -1) {
            bool.filter(f -> f
                    .range(r -> r
                            .number(n -> n
                                    .field("price")
                                    .lte(filters.getMaxPrice().doubleValue())
                            )
                    )
            );
        }

        Aggregation min = AggregationBuilders.min(m -> m.field("price"));
        Aggregation max = AggregationBuilders.max(m -> m.field("price"));

        NativeQuery searchQuery = NativeQuery.builder()
                .withQuery(q -> q.bool(bool.build()))
                .withAggregation("min_price", min)
                .withAggregation("max_price", max)
                .withMaxResults(20)
                .build();

        SearchHits<ProductDocument> searchHits = operations.search(searchQuery, ProductDocument.class);

        var minPrice = ((ElasticsearchAggregations) searchHits.getAggregations())
                .get("min_price")
                .aggregation()
                .getAggregate();

        var maxPrice = ((ElasticsearchAggregations) searchHits.getAggregations())
                .get("max_price")
                .aggregation()
                .getAggregate();

        log.info("search result: {}", searchHits);
        for (SearchHit<ProductDocument> searchHit : searchHits) {
            ProductDocument pd = searchHit.getContent();
            log.info("brand: {}", pd.getBrand());
            log.info("price: {}", pd.getPrice());
            log.info("=================================");
        }
        log.info("aggs: {}, {}", minPrice, maxPrice);
        return searchHits.getSearchHits().stream().map(SearchHit::getContent).toList();
    }
}

