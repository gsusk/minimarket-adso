package org.adso.minimarket.controller.api;

import org.adso.minimarket.constant.UserRoutes;
import org.adso.minimarket.dto.response.UserResponse;
import org.adso.minimarket.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserControllerImpl implements UserController {

    private final UserService userService;

    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(UserRoutes.GET_USER)
    public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}