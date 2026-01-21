package org.adso.minimarket.service;

import org.adso.minimarket.dto.SearchFilters;
import org.adso.minimarket.models.document.ProductDocument;
import org.adso.minimarket.models.product.Product;

public interface SearchService {
    void saveIndex(Product product);
    void searchWithFilters(SearchFilters filters, String query);
}
