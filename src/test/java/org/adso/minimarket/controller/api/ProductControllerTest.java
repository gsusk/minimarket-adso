package org.adso.minimarket.controller.api;

import org.adso.minimarket.dto.request.CreateProductRequest;
import org.adso.minimarket.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(ProductControllerImpl.class)
@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @InjectMocks
    private ProductControllerImpl ProductController;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_withValidRequest_returns201() throws Exception {
        CreateProductRequest request =
                CreateProductRequest.builder().name("Camiseta").price(new BigDecimal(1000)).categoryId(1L).build();

        when(productService.createProduct(any(CreateProductRequest.class))).thenReturn(1L);

        mockMvc.perform(
                        post("/product").content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated()).andExpect(header().exists("Location"));

        verify(productService).createProduct(any(CreateProductRequest.class));
    }

}
