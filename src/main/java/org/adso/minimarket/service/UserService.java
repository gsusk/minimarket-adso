package org.adso.minimarket.service;

import org.adso.minimarket.dto.RegisterRequest;


import org.adso.minimarket.dto.UserResponse;
import org.adso.minimarket.models.User;

public interface UserService {
    User createUser(RegisterRequest registerRequest);
    UserResponse getUserByEmail(String email);
    UserResponse getUserById(Long id);
    User getUserInternalByEmail(String email);
}
