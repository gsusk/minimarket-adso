package org.adso.minimarket.service;

import org.adso.minimarket.dto.CreateProductRequest;
import org.adso.minimarket.dto.DetailedProduct;
import org.adso.minimarket.event.ProductCreatedEventPublisher;
import org.adso.minimarket.exception.NotFoundException;
import org.adso.minimarket.mappers.ProductMapper;
import org.adso.minimarket.models.Category;
import org.adso.minimarket.models.product.Product;
import org.adso.minimarket.repository.jpa.ProductRepository;
import org.adso.minimarket.validation.ProductAttributeValidator;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final CategoryService categoryService;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductCreatedEventPublisher productCreatedEventPublisher;
    private final ProductAttributeValidator attributeValidator;

    ProductServiceImpl(CategoryService categoryService, ProductRepository productRepository,
                       ProductMapper productMapper, ProductCreatedEventPublisher productCreatedEventPublisher,
                       ProductAttributeValidator attributeValidator) {
        this.categoryService = categoryService;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productCreatedEventPublisher = productCreatedEventPublisher;
        this.attributeValidator = attributeValidator;
    }

    @Override
    public Long createProduct(CreateProductRequest productRequest) {
        Category category = categoryService.getById(productRequest.getCategoryId());

        attributeValidator.validate(productRequest.getSpecifications(), category.getAttributeDefinitions());

        Product product = new Product(
                productRequest.getName(),
                productRequest.getDescription(),
                productRequest.getPrice(),
                productRequest.getStock(),
                category,
                productRequest.getBrand(),
                productRequest.getSpecifications()
        );

        productCreatedEventPublisher.handleProductCreatedEvent(product, category.getName());

        return productRepository.save(product).getId();
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found"));
    }

    @Override
    public DetailedProduct getDetailedProductById(Long id) {
        return productMapper.toDto(productRepository.findDetailedById(id).orElseThrow(
                () -> new NotFoundException("Product not found")
        ));
    }
}
