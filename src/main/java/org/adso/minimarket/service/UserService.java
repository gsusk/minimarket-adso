package org.adso.minimarket.service;

import org.adso.minimarket.dto.request.LoginRequest;
import org.adso.minimarket.dto.request.RegisterRequest;


import org.adso.minimarket.dto.response.UserResponse;

public interface UserService {
    UserResponse createUser(RegisterRequest registerRequest);
    UserResponse getUserByEmail(String email);
}
