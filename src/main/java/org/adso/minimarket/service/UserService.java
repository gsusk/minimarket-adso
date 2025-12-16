package org.adso.minimarket.service;

import org.adso.minimarket.controller.request.RegisterRequest;
import org.adso.minimarket.controller.request.LoginUserRequest;
import org.adso.minimarket.dto.auth.AuthResponse;

public interface UserService {
    AuthResponse createUser(RegisterRequest registerRequest);
    AuthResponse loginUser(LoginUserRequest loginUserRequest);
}
