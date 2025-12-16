package org.adso.minimarket.service;

import org.adso.minimarket.controller.request.RegisterRequest;
import org.adso.minimarket.dto.auth.AuthResponse;

public class AuthServiceImpl implements AuthService {

    @Override
    public AuthResponse register(RegisterRequest registerRequest) {
        return AuthResponse.builder()
                .id(1L)
                .name(registerRequest.name())
                .email(registerRequest.email())
                .build();
    }
}
