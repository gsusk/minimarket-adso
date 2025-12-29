package org.adso.minimarket.service;


import org.adso.minimarket.dto.AuthResponse;
import org.adso.minimarket.dto.LoginRequest;
import org.adso.minimarket.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest registerRequest);

    AuthResponse loginUser(LoginRequest req);

    AuthResponse refresh(String refreshToken);
}
