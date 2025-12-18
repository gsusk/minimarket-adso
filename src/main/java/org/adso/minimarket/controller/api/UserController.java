package org.adso.minimarket.controller.api;

import jakarta.validation.constraints.Min;
import org.adso.minimarket.dto.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;


public interface UserController {
    ResponseEntity<UserResponse> getById(@PathVariable @Min(1L) Long id);
}
