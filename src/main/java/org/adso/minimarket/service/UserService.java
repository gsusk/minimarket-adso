package org.adso.minimarket.service;

import org.adso.minimarket.controller.request.CreateUserRequest;
import org.adso.minimarket.dto.UserDto;

public interface UserService {
    UserDto createUser(CreateUserRequest createUserRequest);
}
