package org.adso.minimarket.controller.api;

import org.adso.minimarket.controller.request.CreateUserRequest;
import org.adso.minimarket.controller.request.LoginUserRequest;
import org.adso.minimarket.dto.UserResponseDto;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;


public interface UserController {
    ResponseEntity<@NonNull UserResponseDto> createUser(CreateUserRequest body);
    ResponseEntity<@NonNull UserResponseDto> loginUser(LoginUserRequest body);
}
