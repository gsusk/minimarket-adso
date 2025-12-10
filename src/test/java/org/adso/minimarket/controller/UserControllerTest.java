package org.adso.minimarket.controller;

import org.adso.minimarket.controller.request.CreateUserRequest;
import org.adso.minimarket.dto.UserDto;
import org.adso.minimarket.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 *  TODO:
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
    void whenPostRequestToUserValid_thenSuccess201() throws Exception {
        CreateUserRequest user = new CreateUserRequest("testname", "mesa", "email@gmail.com", "password123");
        UserDto saved = new UserDto(1L, "testname", "mesa", "email@gmail.com");

        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(saved);

        this.mockMvc
                .perform(
                        post("/api/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user))
                )
                //.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("testname"))
                .andExpect(jsonPath("$.email").value("email@gmail.com"))
                .andExpect(jsonPath("$.password").doesNotHaveJsonPath())
                .andExpect(jsonPath("$.last_name").value("mesa"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void whenPostRequestToUserInvalidInput_shouldFailWith400() throws Exception {
        CreateUserRequest user = new CreateUserRequest(null, "lastname", "emailvalid@gmail.com", "password123");
        this.mockMvc.perform(
                        post("/api/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user))
                )
                //.andDo(print())
                .andExpect(status().isBadRequest());
    }
}
