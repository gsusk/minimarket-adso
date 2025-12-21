package org.adso.minimarket.service;

import org.adso.minimarket.dto.request.ProductRequest;
import org.adso.minimarket.models.Product;
import org.adso.minimarket.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Long createProduct(ProductRequest productRequest) {
        Product p = productRepository.save(new Product());
        return p.getId();
    }
}
