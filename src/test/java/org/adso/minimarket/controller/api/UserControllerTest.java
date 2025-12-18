package org.adso.minimarket.controller.api;

import org.adso.minimarket.constant.UserRoutes;
import org.adso.minimarket.dto.response.UserResponse;
import org.adso.minimarket.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * TODO:
 *      -  Cambiar el CommandLineRunner para no depender de perfiles: <a href="https://www.baeldung.com/spring-junit-prevent-runner-beans-testing-execution">Guia</a>
 */

@ActiveProfiles("test") //
@WebMvcTest
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getUserById_withValidId_returns201() throws Exception {
        // given
        Long userId = 1L;
        UserResponse response = UserResponse.builder()
                .id(userId)
                .name("Santiago")
                .lastName("Atehortua")
                .email("test@test.com")
                .build();


        when(userService.getUserById(userId)).thenReturn(response);

        // when + then
        mockMvc.perform(get(UserRoutes.GET_USER, userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userId))
                .andExpect(jsonPath("$.name").value("Santiago"))
                .andExpect(jsonPath("$.last_name").value("Atehortua"))
                .andExpect(jsonPath("$.email").value("test@test.com"));


        verify(userService).getUserById(any(Long.class));
    }

    @Test
    void getUserById_shouldReturn400WhenIdIsNotNumber() throws Exception {
        mockMvc.perform(get(UserRoutes.GET_USER, "abc"))
                .andExpect(status().isBadRequest());
    }

}
