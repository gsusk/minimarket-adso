package org.adso.minimarket.controller.api;

import jakarta.validation.Valid;
import org.adso.minimarket.dto.request.LoginRequest;
import org.adso.minimarket.dto.request.RegisterRequest;
import org.adso.minimarket.dto.response.AuthResponse;
import org.springframework.http.ResponseEntity;

public interface AuthController {
    ResponseEntity<AuthResponse> login(@Valid LoginRequest loginRequest);
    ResponseEntity<AuthResponse> register(@Valid RegisterRequest registerRequest);
}
