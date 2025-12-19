package org.adso.minimarket.controller.api;

import org.adso.minimarket.constant.AuthRoutes;
import org.adso.minimarket.dto.request.LoginRequest;
import org.adso.minimarket.dto.request.RegisterRequest;
import org.adso.minimarket.dto.response.AuthResponse;
import org.adso.minimarket.exception.WrongCredentialsException;
import org.adso.minimarket.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(AuthControllerImpl.class)
@ExtendWith(SpringExtension.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_shouldLoginUser() throws Exception {
        LoginRequest request = new LoginRequest("test@gmail.com", "pagsswordd");
        AuthResponse response = new AuthResponse(
                1L,
                request.email(),
                request.password()
        );
        System.out.println(objectMapper.writeValueAsString(request));
        when(authService.loginUser(any(LoginRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post(AuthRoutes.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
//                .andExpect(jsonPath("$.accessToken").value("access-token"))
//                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    void login_shouldReturn400_withIncorrectInput() throws Exception {
        LoginRequest request = new LoginRequest("testtest.com", "");

        mockMvc.perform(post(AuthRoutes.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authService);
    }

    @Test
    void login_shouldReturn401_ifNotUserFound() throws Exception {
        LoginRequest request = new LoginRequest("2test@gmail.com", "password123");

        when(authService.loginUser(any(LoginRequest.class))).thenThrow(new WrongCredentialsException("unauthorized"));

        mockMvc.perform(post(AuthRoutes.LOGIN)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isUnauthorized()).andExpect(jsonPath("$.message").value("unauthorized"));

        verify(authService).loginUser(any(LoginRequest.class));
    }

    @Test
    void register_shouldRegisterUser() throws Exception {
        RegisterRequest request = new RegisterRequest("test", "lastname", "test@gmail.com", "password123");
        AuthResponse response = new AuthResponse(
                1L,
                request.name(),
                request.password()
        );
        when(authService.register(any(RegisterRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post(AuthRoutes.REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
//                .andExpect(jsonPath("$.accessToken").value("access-token"))
//                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    void register_shouldReturn400_withIncorrectInput() throws Exception {
        RegisterRequest request = new RegisterRequest("testtest.com", "lastname", "test@@.com", "");

        mockMvc.perform(post(AuthRoutes.REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authService);
    }
}