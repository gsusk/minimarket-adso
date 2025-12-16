package org.adso.minimarket.service;

import org.adso.minimarket.controller.request.RegisterRequest;
import org.adso.minimarket.dto.auth.AuthResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
public class AuthServiceTest {

    @Configuration
    static class CreateAuthContextConfiguration {
        @Bean
        public AuthService authService() {
            return new AuthServiceImpl();
        }
    }

    @Autowired
    private AuthService authService;

    @Test
    void whenRegisterCalledCorrectCredentials_itReturns201() {
        RegisterRequest req = new RegisterRequest("test", "lastname", "test@gmail.com", "password123");

        AuthResponse registerResponse = authService.register(req);



        assertEquals("test", registerResponse.getName());
        assertEquals("test@gmail.com", registerResponse.getEmail());
        assertEquals(1L, registerResponse.getId());
    }
}
