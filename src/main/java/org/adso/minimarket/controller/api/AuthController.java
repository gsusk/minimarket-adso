package org.adso.minimarket.controller.api;

import jakarta.validation.Valid;
import org.adso.minimarket.dto.LoginRequest;
import org.adso.minimarket.dto.RegisterRequest;
import org.adso.minimarket.dto.AuthResponse;
import org.springframework.http.ResponseEntity;

public interface AuthController {
    ResponseEntity<AuthResponse> login(@Valid LoginRequest loginRequest);
    ResponseEntity<AuthResponse> register(@Valid RegisterRequest registerRequest);
}
