package org.adso.minimarket.service;

import org.adso.minimarket.dto.request.RegisterRequest;


import org.adso.minimarket.dto.response.UserResponse;
import org.adso.minimarket.models.User;

public interface UserService {
    UserResponse createUser(RegisterRequest registerRequest);
    UserResponse getUserByEmail(String email);
    UserResponse getUserById(Long id);
    User getUserInternalByEmail(String email);
}
