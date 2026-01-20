package org.adso.minimarket.service;

import org.adso.minimarket.dto.CreateProductRequest;
import org.adso.minimarket.dto.DetailedProduct;
import org.adso.minimarket.exception.NotFoundException;
import org.adso.minimarket.mappers.ProductMapper;
import org.adso.minimarket.models.Category;
import org.adso.minimarket.models.product.Product;
import org.adso.minimarket.repository.elastic.ProductSearchRepository;
import org.adso.minimarket.repository.jpa.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductServiceImpl implements ProductService {

    private final CategoryService categoryService;
    private final ProductRepository productRepository;
    private final ProductSearchRepository productSearchRepository;
    private final ProductMapper productMapper;

    ProductServiceImpl(CategoryService categoryService, ProductRepository productRepository, ProductSearchRepository productSearchRepository,
                       ProductMapper productMapper) {
        this.categoryService = categoryService;
        this.productRepository = productRepository;
        this.productSearchRepository = productSearchRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Long createProduct(CreateProductRequest productRequest) {
        Category category = categoryService.getById(productRequest.getCategoryId());

        Product product = new Product(
                productRequest.getName(),
                productRequest.getDescription(),
                new BigDecimal(productRequest.getPrice()),
                productRequest.getStock(),
                category,
                productRequest.getAttributes(),
                productRequest.getCategoryName()
        );
        Product newProduct = product;
        productSearchRepository.save(product);
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
