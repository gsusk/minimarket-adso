package org.adso.minimarket.service;

import org.adso.minimarket.dto.request.CreateProductRequest;

public interface ProductService {
    Long createProduct(CreateProductRequest productRequest);
}
