package org.adso.minimarket.controller.api;

import org.adso.minimarket.config.UserPrincipal;
import org.adso.minimarket.dto.ShoppingCart;
import org.adso.minimarket.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class CartControllerImpl implements CartController {
    private final CartService cartService;

    public CartControllerImpl(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    @GetMapping("/cart")
    public ResponseEntity<ShoppingCart> getCart(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestHeader(value = "X-CGuest-Id", required = false) UUID guestId) {
        Long userId = userPrincipal == null ? null : userPrincipal.getId();
        System.out.println(userPrincipal == null ? null : userPrincipal.toString());
        System.out.println("==>" + " " + guestId);
        return ResponseEntity.ok(cartService.getCart(userId, guestId));
    }
}
