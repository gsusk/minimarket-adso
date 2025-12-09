package org.adso.minimarket.service;

import org.adso.minimarket.controller.request.CreateUserRequest;
import org.adso.minimarket.dto.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public interface UserService {
    UserDto createUser(CreateUserRequest usr);
}
