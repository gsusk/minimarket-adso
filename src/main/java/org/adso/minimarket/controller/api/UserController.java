package org.adso.minimarket.controller.api;

import org.adso.minimarket.controller.request.CreateUserRequest;
import org.adso.minimarket.dto.UserDto;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


@Component
public interface UserController {
    ResponseEntity<@NonNull UserDto> createUser(CreateUserRequest body);
}
