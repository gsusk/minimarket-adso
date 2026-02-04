package org.adso.minimarket.service;

import org.adso.minimarket.dto.CreateProductRequest;
import org.adso.minimarket.dto.DetailedProduct;
import org.adso.minimarket.event.ProductCreatedEventPublisher;
import org.adso.minimarket.exception.BadRequestException;
import org.adso.minimarket.exception.NotFoundException;
import org.adso.minimarket.mappers.ProductMapper;
import org.adso.minimarket.models.Category;
import org.adso.minimarket.models.product.Product;
import org.adso.minimarket.repository.jpa.ProductRepository;
import org.springframework.data.jpa.domain.PredicateSpecification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    private final CategoryService categoryService;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final ProductCreatedEventPublisher productCreatedEventPublisher;

    ProductServiceImpl(CategoryService categoryService, ProductRepository productRepository,
                       ProductMapper productMapper, ProductCreatedEventPublisher productCreatedEventPublisher) {
        this.categoryService = categoryService;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.productCreatedEventPublisher = productCreatedEventPublisher;
    }

    @Override
    public Long createProduct(CreateProductRequest productRequest) {
        Category category = categoryService.getById(productRequest.getCategoryId());
        
        validateAttributes(productRequest.getSpecifications(), category.getAttributeDefinitions());

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

    private void validateAttributes(Map<String, Object> attributes, List<Map<String, Object>> definitions) {
        if (definitions == null || definitions.isEmpty()) {
            return;
        }

        for (java.util.Map<String, Object> def : definitions) {
            String name = (String) def.get("name");
            boolean required = (boolean) def.getOrDefault("required", false);
            String type = (String) def.get("type");

            if (required && (attributes == null || !attributes.containsKey(name))) {
                throw new BadRequestException("Missing required attribute: " + name);
            }

            if (attributes != null && attributes.containsKey(name)) {
                Object value = attributes.get(name);
                validateType(name, value, type);
                validateEnum(name, value, def.get("options"));
                validateRange(name, value, def.get("min"), def.get("max"));
            }
        }
    }

    private void validateType(String name, Object value, String type) {
        switch (type.toLowerCase()) {
            case "string":
                if (!(value instanceof String)) {
                    throw new BadRequestException("Attribute " + name + " must be a string");
                }
                break;
            case "number":
                if (!(value instanceof Number)) {
                    throw new BadRequestException("Attribute " + name + " must be a number");
                }
                break;
            case "boolean":
                if (!(value instanceof Boolean)) {
                    throw new BadRequestException("Attribute " + name + " must be a boolean");
                }
                break;
            default:
                break;
        }
    }

    private void validateEnum(String name, Object value, Object optionsObj) {
        if (optionsObj instanceof java.util.List<?>) {
            List<?> options = (java.util.List<?>) optionsObj;
            if (!options.contains(value)) {
                if (value instanceof Number) {
                   boolean found = options.stream()
                           .filter(opt -> opt instanceof Number)
                           .map(opt -> ((Number) opt).doubleValue())
                           .anyMatch(optVal -> Double.compare(optVal, ((Number) value).doubleValue()) == 0);
                   if (!found) {
                       throw new BadRequestException("Attribute " + name + " has invalid value: " + value + ". Allowed: " + options);
                   }
                } else {
                    throw new BadRequestException("Attribute " + name + " has invalid value: " + value + ". Allowed: " + options);
                }
            }
        }
    }

    private void validateRange(String name, Object value, Object minObj, Object maxObj) {
        if (value instanceof Number) {
            double doubleVal = ((Number) value).doubleValue();

            if (minObj instanceof Number) {
                double min = ((Number) minObj).doubleValue();
                if (doubleVal < min) {
                    throw new BadRequestException("Attribute " + name + " must be >= " + min);
                }
            }

            if (maxObj instanceof Number) {
                double max = ((Number) maxObj).doubleValue();
                if (doubleVal > max) {
                    throw new BadRequestException("Attribute " + name + " must be <= " + max);
                }
            }
        }
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
