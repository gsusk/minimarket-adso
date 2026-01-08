package org.adso.minimarket.service;

import org.adso.minimarket.dto.AddCartItemRequest;
import org.adso.minimarket.dto.ShoppingCart;
import org.adso.minimarket.models.Cart;

import java.util.UUID;

public interface CartService {
    Cart getCart(Long userId, UUID guestId);

    ShoppingCart getShoppingCart(Long userId, UUID guestId);

    void mergeCarts(Long userId, UUID guestId);

    Cart createCart(Long userId);

    Cart createGuestCart(UUID guestId);

    ShoppingCart addItemToCart(Long userId, UUID guestId, AddCartItemRequest request);

    Cart createGuestCart();

    ShoppingCart removeItemFromCart(Long userId, UUID guestId, Long itemId);

    ShoppingCart updateItemQuantity(Long userId, UUID guestId, Long productId, Integer quantity);
}
