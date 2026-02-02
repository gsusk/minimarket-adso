package org.adso.minimarket.service;

import co.elastic.clients.elasticsearch._types.aggregations.AggregationBuilders;
import lombok.extern.slf4j.Slf4j;
import org.adso.minimarket.dto.SearchFilters;
import org.adso.minimarket.models.document.ProductDocument;
import org.adso.minimarket.repository.elastic.ProductSearchRepository;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchAggregations;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
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
        NativeQueryBuilder natQuery = NativeQuery.builder()
                .withQuery(q -> q
                        .match(m -> m
                                .field("name")
                                .fuzziness("1")
                                .query(query)
                        )
                );

        if (filters.getBrand() != null) {
            natQuery.withQuery(q -> q
                    .bool(b -> b
                            .filter(f -> f
                                    .term(t -> t
                                            .field("specifications.brand")
                                            .value(filters.getBrand())
                                    )
                            )
                    )
            );
        }

        natQuery.withAggregation("min_price", AggregationBuilders.min(m -> m.field("price")))
                .withAggregation("max_price", AggregationBuilders.max(m -> m.field("price")));

        if (filters.getMinPrice() != null && filters.getMinPrice().compareTo(BigDecimal.ZERO) > 0) {
            natQuery.withFilter(f -> f
                    .range(r -> r
                            .number(n -> n
                                    .field("price")
                                    .gte(filters.getMinPrice().doubleValue())
                            )
                    )
            );
        }

        if (filters.getMaxPrice() != null && filters.getMaxPrice().compareTo(BigDecimal.ZERO) > 0) {
            natQuery.withFilter(f -> f
                    .range(r -> r
                            .number(n -> n
                                    .field("price")
                                    .lte(filters.getMaxPrice().doubleValue())
                            )
                    )
            );
        }


        Query searchQuery = natQuery.withMaxResults(20).build();

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
            log.info("name: {}", pd.getName());
            log.info("brand: {}", pd.getBrand());
            log.info("price: {}", pd.getPrice());
            log.info("=================================");
        }
        log.info("aggs: {}, {}", minPrice, maxPrice);
        return searchHits.getSearchHits().stream().map(SearchHit::getContent).toList();
    }
}

