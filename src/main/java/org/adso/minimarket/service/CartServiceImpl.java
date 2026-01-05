package org.adso.minimarket.service;

import org.adso.minimarket.dto.ShoppingCart;
import org.adso.minimarket.exception.NotFoundException;
import org.adso.minimarket.mappers.CartMapper;
import org.adso.minimarket.models.Cart;
import org.adso.minimarket.models.CartItem;
import org.adso.minimarket.models.CartStatus;
import org.adso.minimarket.models.User;
import org.adso.minimarket.repository.CartRepository;
import org.adso.minimarket.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

// TODO:
//  - Unir los carritos
@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, CartMapper cartMapper
    ) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
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

        throw new NotFoundException("Cart not found");
    }

    @Override
    public void mergeCarts(Long userId, UUID guestId) {
        Cart guestCart = cartRepository.findCartByGuestIdAndStatus(guestId, CartStatus.ACTIVE).orElse(null);
        Cart userCart = cartRepository.findCartByUserIdAndStatus(userId, CartStatus.ACTIVE).orElseGet(
                () -> this.createCart(userId)
        );

        if (guestCart == null) return;

        for (CartItem guestItem : guestCart.getCartItems()) {
            Optional<CartItem> repeatedItem = userCart.getCartItems()
                    .stream()
                    .filter((item) -> item.getId() == guestItem.getId())
                    .findFirst();

            if (repeatedItem.isPresent()) {
                repeatedItem.get().addToQuantity(repeatedItem.get().getQuantity());
            } else {
                guestItem.setCart(userCart);
                userCart.getCartItems().add(guestItem);
            }
        }

        cartRepository.delete(guestCart);
    }

    @Override
    public Cart createCart(Long userId) {
        cartRepository.findCartsByUserId(userId).forEach(cart -> cart.setStatus(CartStatus.ABANDONED));
        User user = userRepository.getReferenceById(userId);
        Cart cart = new Cart(CartStatus.ACTIVE, user, null);
        return cartRepository.save(cart);
    }
}
