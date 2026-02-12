package org.adso.minimarket.service;

import org.adso.minimarket.dto.CreateProductRequest;
import org.adso.minimarket.dto.DetailedProduct;
import org.adso.minimarket.event.ProductCreatedEventPublisher;
import org.adso.minimarket.exception.NotFoundException;
import org.adso.minimarket.mappers.ProductMapper;
import org.adso.minimarket.models.Category;
import org.adso.minimarket.models.inventory.TransactionType;
import org.adso.minimarket.models.product.Product;
import org.adso.minimarket.repository.jpa.ProductRepository;
import org.adso.minimarket.validation.ProductAttributeValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {

    private final CategoryService categoryService;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductCreatedEventPublisher productCreatedEventPublisher;
    private final ProductAttributeValidator attributeValidator;
    private final InventoryService inventoryService;

    ProductServiceImpl(CategoryService categoryService, ProductRepository productRepository,
                       ProductMapper productMapper, ProductCreatedEventPublisher productCreatedEventPublisher,
                       ProductAttributeValidator attributeValidator, InventoryService inventoryService) {
        this.categoryService = categoryService;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productCreatedEventPublisher = productCreatedEventPublisher;
        this.attributeValidator = attributeValidator;
        this.inventoryService = inventoryService;
    }

    @Override
    @Transactional
    public Long createProduct(CreateProductRequest productRequest) {
        Category category = categoryService.getById(productRequest.getCategoryId());

        attributeValidator.validate(productRequest.getSpecifications(), category.getAllAttributeDefinitions());

        Integer initialStock = productRequest.getStock();

        Product product = new Product(
                productRequest.getName(),
                productRequest.getDescription(),
                productRequest.getPrice(),
                0,
                category,
                productRequest.getBrand(),
                productRequest.getSpecifications()
        );


        Product savedProduct = productRepository.save(product);

        if (initialStock != null && initialStock > 0) {
            inventoryService.adjustStock(
                    savedProduct.getId(),
                    initialStock,
                    TransactionType.RESTOCK,
                    "Initial Stock"
            );
        }

        productCreatedEventPublisher.handleProductCreatedEvent(product, category.getName());
        return savedProduct.getId();
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
