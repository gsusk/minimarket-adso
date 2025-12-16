package org.adso.minimarket.service;

import org.adso.minimarket.controller.request.RegisterRequest;
import org.adso.minimarket.dto.auth.AuthResponse;

public interface AuthService {
    public AuthResponse register(RegisterRequest registerRequest);
}
