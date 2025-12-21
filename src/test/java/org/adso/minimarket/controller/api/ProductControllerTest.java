package org.adso.minimarket.controller.api;

import org.adso.minimarket.dto.request.ProductRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(ProductControllerImpl.class)
@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {

    @InjectMocks
    private ProductControllerImpl ProductController;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create_withValidRequest_returns201() throws Exception {
        ProductRequest request =
                ProductRequest.builder().name("Camiseta").price(new BigDecimal(1000)).categoryId(1L).build();

        mockMvc.perform(
                        post("/product").content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated());
    }

}
