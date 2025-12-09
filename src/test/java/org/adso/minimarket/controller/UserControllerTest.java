package org.adso.minimarket.controller;

import org.adso.minimarket.controller.api.UserControllerImpl;
import org.adso.minimarket.controller.request.CreateUserRequest;
import org.adso.minimarket.dto.UserDto;
import org.adso.minimarket.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserControllerImpl.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserServiceImpl userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenPostRequestToUserValid_thenSuccess201() throws Exception {
        CreateUserRequest user = new CreateUserRequest("testname", "mesa", "email@gmail.com", "password123");
        UserDto userDto = new UserDto(1L, "testname", "mesa", "email@gmail.com");

        when(userService.createUser(any(CreateUserRequest.class))).thenReturn(userDto);

        this.mockMvc
                .perform(
                        post("/api/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(("{\"name\":\"testname\",\"last_name\":\"mesa\",\"email\":\"email@gmail.com\",\"password\":\"password123\",\"extra\":\"hello\"}"))
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
    void whenPostRequestToUserInvalid_shouldFailWith400() throws Exception {
        CreateUserRequest user = new CreateUserRequest(null, "lastname", "emailvalid@gmail.com", "password123");
        this.mockMvc.perform(
                        post("/api/user")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(user))
                ).andDo(print())
                .andExpect(status().isBadRequest());
    }
}
