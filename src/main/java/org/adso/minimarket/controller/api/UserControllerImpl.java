package org.adso.minimarket.controller.api;

import jakarta.validation.Valid;
import org.adso.minimarket.config.UserPrincipal;
import org.adso.minimarket.dto.BasicUser;
import org.adso.minimarket.dto.DetailedUser;
import org.adso.minimarket.dto.UserUpdateRequest;
import org.adso.minimarket.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
public class UserControllerImpl implements UserController {

    private final UserService userService;

    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    @GetMapping("/user/me")
    public ResponseEntity<BasicUser> getMe(@AuthenticationPrincipal(errorOnInvalidType = true) UserPrincipal userPrincipal) {
        return ResponseEntity.ok(userService.getBasicUserById(userPrincipal.getId()));
    }

    @Override
    @GetMapping("/user/profile")
    public ResponseEntity<DetailedUser> getProfile(UserPrincipal userPrincipal) {
        return null;
    }

    @Override
    @PutMapping("/user/profile")
    public ResponseEntity<DetailedUser> updateProfile(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                      @Valid UserUpdateRequest userUpdateRequest) {
        return ResponseEntity.ok(userService.updateUserProfile(userUpdateRequest, userPrincipal.getId()));
    }
}