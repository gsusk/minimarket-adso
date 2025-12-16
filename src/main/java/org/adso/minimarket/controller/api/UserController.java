package org.adso.minimarket.controller.api;

import org.adso.minimarket.controller.request.RegisterRequest;
import org.adso.minimarket.controller.request.LoginUserRequest;
import org.adso.minimarket.dto.auth.AuthResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;


public interface UserController {
    ResponseEntity<@NonNull AuthResponse> createUser(RegisterRequest body);
    ResponseEntity<@NonNull AuthResponse> loginUser(LoginUserRequest body);
}
