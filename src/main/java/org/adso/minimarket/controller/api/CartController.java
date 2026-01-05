package org.adso.minimarket.controller.api;

import org.adso.minimarket.config.UserPrincipal;
import org.adso.minimarket.dto.ShoppingCart;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

public interface CartController {
    ResponseEntity<ShoppingCart> getCart(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                         @RequestHeader("X-CGuest-Id") UUID uuid);
}
