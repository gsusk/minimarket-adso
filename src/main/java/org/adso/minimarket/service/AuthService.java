package org.adso.minimarket.service;


import org.adso.minimarket.dto.AuthResponse;
import org.adso.minimarket.dto.LoginRequest;
import org.adso.minimarket.dto.RegisterRequest;

import java.util.UUID;

public interface AuthService {
    AuthResponse register(RegisterRequest registerRequest, UUID guestId);

    AuthResponse loginUser(LoginRequest req, UUID guestId);

    AuthResponse refresh(String refreshToken);
}
