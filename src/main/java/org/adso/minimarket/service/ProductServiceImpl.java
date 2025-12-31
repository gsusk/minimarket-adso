package org.adso.minimarket.service;

import org.adso.minimarket.dto.CreateProductRequest;
import org.adso.minimarket.dto.DetailedProduct;
import org.adso.minimarket.exception.NotFoundException;
import org.adso.minimarket.mappers.ProductMapper;
import org.adso.minimarket.models.Category;
import org.adso.minimarket.models.Product;
import org.adso.minimarket.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductServiceImpl implements ProductService {

    private final CategoryService categoryService;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    ProductServiceImpl(CategoryService categoryService, ProductRepository productRepository,
                       ProductMapper productMapper) {
        this.categoryService = categoryService;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Long createProduct(CreateProductRequest productRequest) {
        Category category = categoryService.getInternalById(productRequest.getCategoryId());

        Product product = new Product(
                productRequest.getName(),
                productRequest.getDescription(),
                new BigDecimal(productRequest.getPrice()),
                category
        );

        return productRepository.save(product).getId();
    }

    @Override
    public DetailedProduct getProductById(Long id) {
        return productMapper.toDto(productRepository.findDetailedById(id).orElseThrow(
                () -> new NotFoundException("Product not found")
        ));
    }
}
