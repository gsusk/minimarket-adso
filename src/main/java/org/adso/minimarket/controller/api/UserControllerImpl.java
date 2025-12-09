package org.adso.minimarket.controller.api;

import jakarta.validation.Valid;
import org.adso.minimarket.controller.request.CreateUserRequest;
import org.adso.minimarket.dto.UserDto;
import org.adso.minimarket.service.UserServiceImpl;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserControllerImpl implements UserController {

    private final UserServiceImpl userService;

    public UserControllerImpl(UserServiceImpl userService) {
        this.userService = userService;
    }

    @ResponseBody
    @PostMapping(value = "/user")
    public ResponseEntity<@NonNull UserDto> createUser(@Valid @RequestBody CreateUserRequest body) {
        System.out.println("<+++" +" " + body);
        UserDto user = userService.createUser(body);
        return ResponseEntity.status(HttpStatus.CREATED).contentType(MediaType.APPLICATION_JSON).body(user);
    }
}

