package org.adso.minimarket.controller.api;

import org.adso.minimarket.config.UserPrincipal;
import org.adso.minimarket.dto.BasicUser;
import org.adso.minimarket.dto.DetailedUser;
import org.adso.minimarket.dto.UserUpdateRequest;
import org.springframework.http.ResponseEntity;

public interface UserController {
    ResponseEntity<BasicUser> getMe(UserPrincipal userPrincipal);

    ResponseEntity<DetailedUser> getProfile(UserPrincipal userPrincipal);

    ResponseEntity<DetailedUser> updateProfile(UserPrincipal userPrincipal, UserUpdateRequest userUpdateRequest);
}
