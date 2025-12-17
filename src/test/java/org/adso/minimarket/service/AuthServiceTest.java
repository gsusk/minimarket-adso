package org.adso.minimarket.service;


import lombok.Setter;
import org.adso.minimarket.dto.request.LoginRequest;
import org.adso.minimarket.dto.request.RegisterRequest;
import org.adso.minimarket.dto.response.AuthResponse;
import org.adso.minimarket.dto.response.UserResponse;
import org.adso.minimarket.exception.WrongCredentialsException;
import org.adso.minimarket.mappers.AuthMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AuthServiceTest {

    @Configuration
    static class CreateAuthContextConfiguration {
        @Bean
        public AuthService authService(UserService userService, AuthMapper authMapper, PasswordEncoder passwordEncoder) {
            return new AuthServiceImpl(userService, authMapper, passwordEncoder);
        }
    }

    @Autowired
    private AuthService authService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private AuthMapper authMapper;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Test
    void whenRegisterCalledCorrectCredentials_itReturns201() throws Exception {
        RegisterRequest req = new RegisterRequest("test", "lastname", "test@gmail.com", "password123");
        AuthResponse exp = AuthResponse.builder()
                .id(1L)
                .name(req.name())
                .email(req.email())
                .build();
        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .name(req.name())
                .lastName(req.lastName())
                .email(req.email())
                .password(req.password())
                .build();

        when(userService.createUser(any(RegisterRequest.class))).thenReturn(userResponse);
        when(authMapper.toAuthResponseDto(any())).thenReturn(exp);
        when(passwordEncoder.encode(any(String.class))).thenReturn(any(String.class));

        AuthResponse registerResponse = authService.register(req);

        assertEquals("test", registerResponse.getName());
        assertEquals("test@gmail.com", registerResponse.getEmail());
        assertEquals(1L, registerResponse.getId());

        verify(userService).createUser(any(RegisterRequest.class));
        verify(authMapper).toAuthResponseDto(any(UserResponse.class));
        verify(passwordEncoder).encode(any(String.class));
    }

    @Test
    void whenLoginCalled_thenReturnsFoundUser() throws Exception {
        UserResponse exp = UserResponse.builder()
                .id(1L)
                .name("test")
                .email("test@gmail.com")
                .password("password123!")
                .lastName("lastname")
                .build();
        LoginRequest req = new LoginRequest(exp.getEmail(), exp.getPassword());
        AuthResponse dto = AuthResponse.builder()
                .name(exp.getName())
                .email(exp.getEmail())
                .id(exp.getId())
                .build();

        when(userService.getUserByEmail(any(String.class))).thenReturn(exp);
        when(authMapper.toAuthResponseDto(any(UserResponse.class))).thenReturn(dto);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        AuthResponse got = authService.loginUser(req);

        assertEquals("test", got.getName());
        assertEquals("test@gmail.com", got.getEmail());
        assertEquals(1L, got.getId());

        verify(userService).getUserByEmail(any(String.class));
        verify(passwordEncoder).matches(anyString(), anyString());
        verify(authMapper).toAuthResponseDto(any(UserResponse.class));
    }


    @Test
    void throwsWrongCredentialsException_whenLoginPasswordIsIncorrect() throws Exception {
        LoginRequest req = new LoginRequest("test@gmail.com", "password123");
        UserResponse usr = UserResponse.builder()
                .id(1L)
                .name("test")
                .email(req.email())
                .password(req.password())
                .build();

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        when(userService.getUserByEmail(any(String.class))).thenReturn(usr);

        assertThrows(WrongCredentialsException.class, () -> authService.loginUser(req));

        verify(passwordEncoder).matches(anyString(), anyString());
        verify(userService).getUserByEmail(any(String.class));
    }
}
