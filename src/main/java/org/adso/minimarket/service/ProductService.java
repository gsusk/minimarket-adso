package org.adso.minimarket.service;

import org.adso.minimarket.dto.CreateProductRequest;
import org.adso.minimarket.dto.DetailedProduct;

public interface ProductService {
    Long createProduct(CreateProductRequest productRequest);
    DetailedProduct getProductById(Long id);
}
