package org.adso.minimarket.controller.api;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.adso.minimarket.config.UserPrincipal;
import org.adso.minimarket.dto.AddCartItemRequest;
import org.adso.minimarket.dto.ShoppingCart;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface CartController {
    ResponseEntity<ShoppingCart> getCart(UserPrincipal userPrincipal,
                                         UUID guestId);

    ResponseEntity<ShoppingCart> addItem(UserPrincipal userPrincipal,
                                         @Valid AddCartItemRequest body,
                                         UUID guestId,
                                         HttpServletRequest request);
}
