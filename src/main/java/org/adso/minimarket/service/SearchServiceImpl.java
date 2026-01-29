package org.adso.minimarket.service;

import org.adso.minimarket.dto.SearchFilters;
import org.adso.minimarket.models.document.ProductDocument;
import org.adso.minimarket.repository.elastic.ProductSearchRepository;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

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
    public void searchWithFilters(SearchFilters filters, String query) {
    }
}
