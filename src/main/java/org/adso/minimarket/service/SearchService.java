package org.adso.minimarket.service;

import org.adso.minimarket.dto.SearchFilters;
import org.adso.minimarket.models.document.ProductDocument;

import java.util.List;

public interface SearchService {
    void saveIndex(ProductDocument product);

    List<ProductDocument> searchWithFilters(SearchFilters filters, String query);
}
