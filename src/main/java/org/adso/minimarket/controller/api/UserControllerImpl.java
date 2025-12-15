package org.adso.minimarket.controller.api;

import jakarta.validation.Valid;
import org.adso.minimarket.constant.UserRoutes;
import org.adso.minimarket.controller.request.CreateUserRequest;
import org.adso.minimarket.controller.request.LoginUserRequest;
import org.adso.minimarket.dto.UserResponseDto;
import org.adso.minimarket.service.UserService;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UserRoutes.BASE)
public class UserControllerImpl implements UserController {


    private final UserService userService;

    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }


    @PostMapping(UserRoutes.REGISTER)
    public ResponseEntity<@NonNull UserResponseDto> createUser(@Valid @RequestBody CreateUserRequest body) {
        UserResponseDto user = userService.createUser(body);
        return new ResponseEntity<@NonNull UserResponseDto>(user, HttpStatus.CREATED);
    }

    @PostMapping(UserRoutes.LOGIN)
    public ResponseEntity<@NonNull UserResponseDto> loginUser(@Valid @RequestBody LoginUserRequest body) {
        UserResponseDto user = userService.loginUser(body);
        return new ResponseEntity<UserResponseDto>(user, HttpStatus.OK);
    }
}