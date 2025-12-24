package org.adso.minimarket.controller.api;

import org.adso.minimarket.dto.CreateProductRequest;
import org.adso.minimarket.dto.ProductResponse;
import org.adso.minimarket.models.Category;
import org.adso.minimarket.models.Product;
import org.adso.minimarket.service.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                CreateProductRequest.builder().name("Camiseta").price("1000").categoryId(1L).build();

        when(productService.createProduct(any(CreateProductRequest.class))).thenReturn(1L);

        mockMvc.perform(
                        post("/product").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated()).andExpect(header().exists("Location"));

        verify(productService).createProduct(any(CreateProductRequest.class));
    }

    @Test
    void create_withInvalidRequest_returns400() throws Exception {
        CreateProductRequest request =
                CreateProductRequest.builder().name("Camiseta").categoryId(-1L).build();

        mockMvc.perform(
                        post("/product").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }

    @Test
    void getById_succeeds_return200() throws Exception {
        long request = 1L;
        Product p = new Product(1L, "test", "desc", new BigDecimal("1000"), new Category());
        ProductResponse pr = new ProductResponse(1L, "test", "desc","1000", List.of(), null, LocalDateTime.now());

        when(productService.getProductById(any(Long.class))).thenReturn(pr);

        mockMvc.perform(
                        get("/product/" + request)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("test"))
                .andExpect(jsonPath("$.price").value(1000));
    }


    @Test
    void getById_fails_withWrongInput() throws Exception {
        long request = -1L;
        String request2 = "abc";
        mockMvc.perform(
                        get("/product/" + request)
                )
                .andExpect(status().isBadRequest());

        mockMvc.perform(
                        get("/product/" + request2)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(productService);
    }
}
