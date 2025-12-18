package org.adso.minimarket.controller.api;

import org.adso.minimarket.dto.request.LoginRequest;
import org.adso.minimarket.dto.request.RegisterRequest;
import org.adso.minimarket.dto.response.AuthResponse;
import org.springframework.http.ResponseEntity;

public interface AuthController {
    ResponseEntity<AuthResponse> login(RegisterRequest registerRequest);

    ResponseEntity<AuthResponse> register(LoginRequest loginRequest);
}
