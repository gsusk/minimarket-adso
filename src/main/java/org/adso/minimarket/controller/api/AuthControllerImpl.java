package org.adso.minimarket.controller.api;

import org.adso.minimarket.dto.request.LoginRequest;
import org.adso.minimarket.dto.request.RegisterRequest;
import org.adso.minimarket.dto.response.AuthResponse;
import org.springframework.http.ResponseEntity;

public class AuthControllerImpl implements AuthController {
    @Override
    public ResponseEntity<AuthResponse> login(RegisterRequest registerRequest) {
        return null;
    }

    @Override
    public ResponseEntity<AuthResponse> register(LoginRequest loginRequest) {
        return null;
    }
}
