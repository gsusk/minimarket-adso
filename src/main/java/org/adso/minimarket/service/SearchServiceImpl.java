package org.adso.minimarket.service;

import org.adso.minimarket.dto.SearchFilters;
import org.adso.minimarket.models.document.ProductDocument;
import org.adso.minimarket.repository.elastic.ProductSearchRepository;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {
    private final ProductSearchRepository searchRepository;

    public SearchServiceImpl(ProductSearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    @Override
    public void saveIndex(ProductDocument product) {
        searchRepository.save(product);
    }

    @Override
    public void searchWithFilters(SearchFilters filters, String query) {
    }
}
