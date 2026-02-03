package org.adso.minimarket.service;

import org.adso.minimarket.dto.SearchFilters;
import org.adso.minimarket.dto.SearchResult;
import org.adso.minimarket.models.document.ProductDocument;

public interface SearchService {
    void saveIndex(ProductDocument product);

    SearchResult searchWithFilters(SearchFilters filters, String query);
}
