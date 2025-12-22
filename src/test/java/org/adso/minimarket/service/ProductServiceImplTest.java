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

import java.math.BigDecimal;

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
    private CategoryRepository categoryRepository;

    @Test
    void createProduct_shouldSaveUser() {
        CreateProductRequest request =
                CreateProductRequest.builder().name("Camiseta").description("").price(new BigDecimal(1000)).categoryId(1L).build();
        Product res =
                new Product(1L, "Camiseta", "", request.getPrice(), new Category());
        Category category = new Category(1L, "Ropa");

        when(productRepository.save(any(Product.class))).thenReturn(res);
        when(categoryRepository.getReferenceById(any(Long.class))).thenReturn(category);

        Long id = productService.createProduct(request);

        assertEquals(1L, id);

        verify(productRepository).save(any(Product.class));
        verify(categoryRepository).getReferenceById(any(Long.class));
    }

    @Test
    void createProduct_failsIfCategoryNotFound() {
        CreateProductRequest request =
                CreateProductRequest.builder().name("Camiseta").description("").price(new BigDecimal(1000)).categoryId(1L).build();

        when(categoryRepository.existsById(any(Long.class))).thenReturn(false);

        assertThrows(NotFoundException.class, () -> productService.createProduct(request));

        verifyNoInteractions(productRepository);
        verify(categoryRepository).existsById(any(Long.class));
    }
}
