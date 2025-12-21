package org.adso.minimarket.service;

import org.adso.minimarket.dto.request.ProductRequest;

public interface ProductService {
    void createProduct(ProductRequest productRequest);
}
