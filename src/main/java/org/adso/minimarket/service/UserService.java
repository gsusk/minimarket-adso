package org.adso.minimarket.service;

import org.adso.minimarket.controller.request.CreateUserRequest;
import org.adso.minimarket.controller.request.LoginUserRequest;
import org.adso.minimarket.dto.UserResponseDto;

public interface UserService {
    UserResponseDto createUser(CreateUserRequest createUserRequest);
    UserResponseDto loginUser(LoginUserRequest loginUserRequest);
}
