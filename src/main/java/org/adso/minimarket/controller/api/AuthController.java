package org.adso.minimarket.controller.api;

import org.adso.minimarket.constant.AuthRoutes;
import org.adso.minimarket.dto.request.LoginRequest;
import org.adso.minimarket.dto.request.RegisterRequest;
import org.adso.minimarket.dto.response.AuthResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

public interface AuthController {
    ResponseEntity<AuthResponse> login(LoginRequest loginRequest);
    ResponseEntity<AuthResponse> register(RegisterRequest registerRequest);
}
