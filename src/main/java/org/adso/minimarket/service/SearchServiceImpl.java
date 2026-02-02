package org.adso.minimarket.service;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.AggregationVariant;
import lombok.extern.slf4j.Slf4j;
import org.adso.minimarket.dto.SearchFilters;
import org.adso.minimarket.models.document.ProductDocument;
import org.adso.minimarket.repository.elastic.ProductSearchRepository;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

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
                .withAggregation("colors", new Aggregation((AggregationVariant) Aggregation.of(b -> b
                        .terms(t -> t
                                .field("colors"))))
                )
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

        Query searchQuery = natQuery.withMaxResults(20).build();

        SearchHits<ProductDocument> searchHits = operations.search(searchQuery, ProductDocument.class);

        log.info("search result: {}", searchHits);
        for (SearchHit<ProductDocument> searchHit : searchHits) {
            ProductDocument pd = searchHit.getContent();
            log.info("search hit product document");
            log.info("name: {}", pd.getName());
            log.info("desc: {}", pd.getDescription());
            log.info("brand: {}", pd.getBrand());
            log.info("att: {}", pd.getSpecifications());
            log.info("=================================");
        }
        return searchHits.getSearchHits().stream().map(SearchHit::getContent).toList();
    }
}

