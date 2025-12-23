package org.adso.minimarket.service;

import org.adso.minimarket.dto.request.CreateProductRequest;
import org.adso.minimarket.models.Product;

import java.util.Optional;

public interface ProductService {
    Long createProduct(CreateProductRequest productRequest);
    Product getProductById(Long id);
}
