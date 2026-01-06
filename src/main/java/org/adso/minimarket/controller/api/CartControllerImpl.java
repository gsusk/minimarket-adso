package org.adso.minimarket.controller.api;

import jakarta.servlet.http.HttpServletRequest;
import org.adso.minimarket.config.UserPrincipal;
import org.adso.minimarket.dto.AddCartItemRequest;
import org.adso.minimarket.dto.ShoppingCart;
import org.adso.minimarket.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
        if (userId == null && guestId == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(cartService.getCart(userId, guestId));
    }

    @Override
    @PostMapping("/cart/items")
    public ResponseEntity<ShoppingCart> addItem(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody AddCartItemRequest body,
            @RequestHeader(value = "X-CGuest-Id", required = false) UUID guestId,
            HttpServletRequest request) {

        Long userId = userPrincipal == null ? null : userPrincipal.getId();
        ShoppingCart cart = cartService.addItemToCart(userId, guestId, body);
        return ResponseEntity.ok(cart);
    }
}
