package org.adso.minimarket.service;

import org.adso.minimarket.dto.request.ProductRequest;
import org.adso.minimarket.dto.request.RegisterRequest;
import org.adso.minimarket.models.Product;
import org.adso.minimarket.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    void createProduct_shouldSaveUser(){
        ProductRequest request = ProductRequest.builder().name("Camiseta").description("").price(new BigDecimal(1000)).categoryId(1L).build();

        productService.createProduct(request);

        verify(productRepository).save(any(Product.class));
    }
}
