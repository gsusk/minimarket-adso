package org.adso.minimarket.service;

import org.adso.minimarket.dto.SearchFilters;
import org.adso.minimarket.models.document.ProductDocument;

public interface SearchService {
    void saveIndex(ProductDocument product);

    void searchWithFilters(SearchFilters filters, String query);
}
