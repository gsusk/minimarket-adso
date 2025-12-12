package org.adso.minimarket.controller.api;

import jakarta.validation.Valid;
import org.adso.minimarket.controller.request.CreateUserRequest;
import org.adso.minimarket.dto.UserDto;
import org.adso.minimarket.service.UserService;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserControllerImpl implements UserController {


    private final UserService userService;

    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<@NonNull UserDto> createUser(@Valid @RequestBody CreateUserRequest body) {
        UserDto user = userService.createUser(body);
        return new ResponseEntity<@NonNull UserDto>(user, HttpStatus.CREATED);
    }
}