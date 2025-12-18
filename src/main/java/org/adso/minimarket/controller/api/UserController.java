package org.adso.minimarket.controller.api;

import jakarta.websocket.server.PathParam;
import org.adso.minimarket.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;


public interface UserController {
    ResponseEntity<UserResponse> getById(Long id);
}
