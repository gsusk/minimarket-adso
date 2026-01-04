package org.adso.minimarket.controller.api;

import org.adso.minimarket.constant.UserRoutes;
import org.adso.minimarket.dto.BasicUser;
import org.adso.minimarket.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class UserControllerImpl implements UserController {

    private final UserService userService;

    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

}