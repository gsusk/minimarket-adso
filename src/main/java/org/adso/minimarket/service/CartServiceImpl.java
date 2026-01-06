package org.adso.minimarket.service;

import jakarta.transaction.Transactional;
import org.adso.minimarket.dto.AddCartItemRequest;
import org.adso.minimarket.dto.ShoppingCart;
import org.adso.minimarket.exception.NotFoundException;
import org.adso.minimarket.mappers.CartMapper;
import org.adso.minimarket.models.*;
import org.adso.minimarket.repository.CartRepository;
import org.adso.minimarket.repository.ProductRepository;
import org.adso.minimarket.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

// TODO:
//  - Unir los carritos
@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;
    private final ProductRepository productRepository;

    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, CartMapper cartMapper,
                           ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.cartMapper = cartMapper;
        this.productRepository = productRepository;
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
    @Transactional
    public void mergeCarts(Long userId, UUID guestId) {
        Cart guestCart = cartRepository.findCartByGuestIdAndStatus(guestId, CartStatus.ACTIVE).orElse(null);
        Cart userCart = cartRepository.findCartByUserIdAndStatus(userId, CartStatus.ACTIVE).orElseGet(
                () -> this.createCart(userId)
        );

        if (guestCart == null || guestCart.getCartItems().isEmpty()) return;

        List<CartItem> itemsToProcess = new ArrayList<>(guestCart.getCartItems());

        for (CartItem guestItem : itemsToProcess) {
            Optional<CartItem> repeatedItem = userCart.getCartItems()
                    .stream()
                    .filter((item) -> item.getProduct().getId().equals(guestItem.getProduct().getId()))
                    .findFirst();

            if (repeatedItem.isPresent()) {
                repeatedItem.get().addToQuantity(guestItem.getQuantity());
            } else {
                CartItem newItem = new CartItem(userCart, guestItem.getProduct(), guestItem.getQuantity());
                userCart.getCartItems().add(newItem);
            }

            guestCart.getCartItems().remove(guestItem);
        }

        guestCart.setStatus(CartStatus.ABANDONED);
        cartRepository.save(userCart);
    }

    @Override
    @Transactional
    public Cart createCart(Long userId) {
        cartRepository.findCartsByUserId(userId).forEach(cart -> {
            if (cart.getStatus() == CartStatus.ACTIVE) cart.setStatus(CartStatus.ABANDONED);
        });

        User user = userRepository.getReferenceById(userId);
        Cart cart = new Cart(user);
        return cartRepository.saveAndFlush(cart);
    }


    public Cart createGuestCart() {
        return cartRepository.save(new Cart(UUID.randomUUID()));
    }

    @Override
    @Transactional
    public ShoppingCart addItemToCart(Long userId, UUID guestId, AddCartItemRequest item) {
        Cart cart;
        ShoppingCart shoppingCart;

        if (userId != null) {
            cart = cartRepository.findCartByUserIdAndStatus(userId, CartStatus.ACTIVE).orElseGet(() -> this.createCart(userId));
        } else {
            cart = cartRepository.findCartByGuestIdAndStatus(guestId, CartStatus.ACTIVE).orElseGet(this::createGuestCart);
        }

        Optional<CartItem> repeated = cart.getCartItems()
                .stream()
                .filter((i) -> i.getProduct().getId() == item.getProductId())
                .findFirst();

        Product product = productRepository.getReferenceById(item.getProductId());

        if (repeated.isPresent()) {
            repeated.get().addToQuantity(repeated.get().getQuantity());
            cart = cartRepository.findShoppingCartByUserIdAndStatus(userId, CartStatus.ACTIVE).orElse(null);
        } else {
            CartItem newCartItem = new CartItem(cart, product, item.getQuantity());
            cart.getCartItems().add(newCartItem);
            cart = cartRepository.findShoppingCartByGuestIdAndStatus(guestId, CartStatus.ACTIVE).orElse(null);
        }

        return null;
    }
}
