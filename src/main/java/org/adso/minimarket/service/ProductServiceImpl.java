package org.adso.minimarket.service;

import org.adso.minimarket.dto.request.ProductRequest;
import org.adso.minimarket.models.Product;
import org.adso.minimarket.repository.ProductRepository;

public class ProductServiceImpl implements ProductService {

    private ProductRepository productRepository;

    ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void createProduct(ProductRequest productRequest) {
        productRepository.save(new Product());
    }
}
