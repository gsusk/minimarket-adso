package org.adso.minimarket.service;

import org.adso.minimarket.dto.ShoppingCart;
import org.adso.minimarket.exception.NotFoundException;
import org.adso.minimarket.mappers.CartMapper;
import org.adso.minimarket.models.Cart;
import org.adso.minimarket.models.CartStatus;
import org.adso.minimarket.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

// TODO:
//  - Unir los carritos
@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    public CartServiceImpl(CartRepository cartRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    @Override
    public ShoppingCart getCart(Long userId, UUID guestId) {
        Optional<Cart> cart;
        if (userId != null) {
            cart = cartRepository.findCartByUserIdAndStatus(userId, CartStatus.ACTIVE);
            if (cart.isEmpty()) {
                throw new NotFoundException("Cart not found");
            }

            return cartMapper.toDto(cart.get());
        }

        if (guestId != null) {
            cart = cartRepository.findCartByGuestIdAndStatus(guestId, CartStatus.ACTIVE);
            if (cart.isEmpty()) {
                throw new NotFoundException("Cart not found");
            }

            return cartMapper.toDto(cart.get());
        }

        return null;
    }
}
