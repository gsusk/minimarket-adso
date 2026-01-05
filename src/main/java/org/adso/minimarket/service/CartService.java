package org.adso.minimarket.service;

import org.adso.minimarket.dto.ShoppingCart;
import org.adso.minimarket.models.Cart;

import java.util.UUID;

public interface CartService {
    ShoppingCart getCart(Long userId, UUID guestId);

    void mergeCarts(Long userId, UUID guestId);

    Cart createCart(Long userId);
}
