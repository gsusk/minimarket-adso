package org.adso.minimarket.controller.api;

import jakarta.validation.Valid;
import org.adso.minimarket.constant.UserRoutes;
import org.adso.minimarket.dto.request.RegisterRequest;
import org.adso.minimarket.dto.response.UserResponse;
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
    public ResponseEntity<@NonNull UserResponse> getUser(@Valid @RequestBody RegisterRequest body) {
        UserResponse user = userService.createUser(body);
        return new ResponseEntity<@NonNull UserResponse>(user, HttpStatus.CREATED);
    }
}