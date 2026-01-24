package org.adso.minimarket.controller.api;

import org.adso.minimarket.config.UserPrincipal;
import org.adso.minimarket.dto.BasicUser;
import org.springframework.http.ResponseEntity;

public interface UserController {
    ResponseEntity<BasicUser> getMe(UserPrincipal userPrincipal);
}
