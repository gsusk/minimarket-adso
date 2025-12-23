package org.adso.minimarket.service;

import org.adso.minimarket.dto.request.CreateProductRequest;
import org.adso.minimarket.exception.NotFoundException;
import org.adso.minimarket.models.Category;
import org.adso.minimarket.models.Product;
import org.adso.minimarket.repository.CategoryRepository;
import org.adso.minimarket.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @Test
    void createProduct_Success() {
        var req = new CreateProductRequest("Camiseta", "blanca", new BigDecimal("1500.0"), 1L);
        var category = new Category(); // Assume ID 1
        var savedProduct = new Product("Camiseta", "blanca", new BigDecimal("1500.0"), category);

        ReflectionTestUtils.setField(savedProduct, "id", 99L);
        when(categoryService.getById(1L)).thenReturn(category);
        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        Long resultId = productService.createProduct(req);

        assertEquals(99L, resultId);
        verify(categoryService).getById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void getProduct_Success() {
        var productId = 1L;
        var product = new Product(1L, "Camiseta", "blanca", new BigDecimal("1000"), new Category());

        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));

        Product result = productService.getProductById(productId);

        assertEquals(1L, result.getId());

        verify(productRepository).findById(any(Long.class));
    }


    @Test
    void getProduct_ifNotFound_failsThrows() {
        var productId = 1L;

        when(productRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> productService.getProductById(productId));

        verify(productRepository).findById(any(Long.class));
    }
}
