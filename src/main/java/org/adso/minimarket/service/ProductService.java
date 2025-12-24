package org.adso.minimarket.service;

import org.adso.minimarket.dto.CreateProductRequest;
import org.adso.minimarket.dto.ProductResponse;
import org.adso.minimarket.models.Product;

public interface ProductService {
    Long createProduct(CreateProductRequest productRequest);
    ProductResponse getProductById(Long id);
}
