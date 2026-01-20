package org.adso.minimarket.service;

import org.adso.minimarket.dto.CreateProductRequest;
import org.adso.minimarket.dto.DetailedProduct;
import org.adso.minimarket.models.product.Product;

public interface ProductService {
    Long createProduct(CreateProductRequest productRequest);

    Product getById(Long id);

    DetailedProduct getDetailedProductById(Long id);
}
