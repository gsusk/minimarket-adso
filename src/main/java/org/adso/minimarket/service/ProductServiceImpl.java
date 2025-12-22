package org.adso.minimarket.service;

import org.adso.minimarket.dto.request.CreateProductRequest;
import org.adso.minimarket.models.Category;
import org.adso.minimarket.models.Product;
import org.adso.minimarket.repository.CategoryRepository;
import org.adso.minimarket.repository.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Long createProduct(CreateProductRequest productRequest) {
        Category category = categoryRepository.getReferenceById(productRequest.getCategoryId());
        Product product = new Product(productRequest.getName(), productRequest.getDescription(), productRequest.getPrice(), category);
        return productRepository.save(product).getId();
    }
}
