package org.adso.minimarket.service;

import org.adso.minimarket.dto.CreateProductRequest;
import org.adso.minimarket.dto.ProductResponse;
import org.adso.minimarket.exception.NotFoundException;
import org.adso.minimarket.mappers.ProductMapper;
import org.adso.minimarket.models.Category;
import org.adso.minimarket.models.Product;
import org.adso.minimarket.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
//    @InjectMocks
//    private ProductServiceImpl productService;
//
//    @Mock
//    private ProductRepository productRepository;
//
//    @Mock
//    private CategoryService categoryService;
//
//    @Mock
//    private ProductMapper productMapper;
//
//    @Test
//    void createProduct_Success() {
//        var req = new CreateProductRequest("Camiseta", "blanca", "1500.0", 1L);
//        var category = new Category(); // Assume ID 1
//        var savedProduct = new Product("Camiseta", "blanca", new BigDecimal("1500.0"), category);
//
//        ReflectionTestUtils.setField(savedProduct, "id", 99L);
//        when(categoryService.getInternalById(1L)).thenReturn(category);
//        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);
//
//        Long resultId = productService.createProduct(req);
//
//        assertEquals(99L, resultId);
//        verify(categoryService).getInternalById(1L);
//        verify(productRepository).save(any(Product.class));
//    }
//
//    @Test
//    void getProduct_Success() {
//        var productId = 1L;
//        var product = new Product(1L, "Camiseta", "blanca", new BigDecimal("1000"), 1, new Category());
//        var productResponse = new ProductResponse(1L, "Camiseta", "blanca", "1000", List.of(),
//                null, 1, LocalDateTime.now());
//
//        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
//        when(productMapper.toDto(any(Product.class))).thenReturn(productResponse);
//
//        ProductResponse result = productService.getProductById(productId);
//
//        assertEquals(1L, result.getId());
//
//        verify(productRepository).findById(any(Long.class));
//        verify(productMapper).toDto(any(Product.class));
//    }
//
//
//    @Test
//    void getProduct_ifNotFound_failsThrows() {
//        var productId = 1L;
//
//        when(productRepository.findById(any(Long.class))).thenReturn(Optional.empty());
//
//        assertThrows(NotFoundException.class, () -> productService.getProductById(productId));
//
//        verify(productRepository).findById(any(Long.class));
//    }
}
