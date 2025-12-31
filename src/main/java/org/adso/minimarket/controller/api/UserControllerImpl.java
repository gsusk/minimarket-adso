package org.adso.minimarket.controller.api;

import jakarta.validation.constraints.Min;
import org.adso.minimarket.constant.UserRoutes;
import org.adso.minimarket.dto.BasicUser;
import org.adso.minimarket.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class UserControllerImpl implements UserController {

    private final UserService userService;

    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(UserRoutes.GET_USER)
    public ResponseEntity<BasicUser> getById(@PathVariable @Min(1) Long id) {
        BasicUser user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}