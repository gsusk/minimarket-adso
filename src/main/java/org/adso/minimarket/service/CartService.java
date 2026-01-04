package org.adso.minimarket.service;

import org.adso.minimarket.dto.ShoppingCart;

import java.util.UUID;

public interface CartService {
    ShoppingCart getCart(Long userId, UUID guestId);
}
