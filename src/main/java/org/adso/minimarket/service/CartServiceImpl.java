package org.adso.minimarket.service;

import jakarta.transaction.Transactional;
import org.adso.minimarket.dto.AddCartItemRequest;
import org.adso.minimarket.dto.ShoppingCart;
import org.adso.minimarket.exception.InternalErrorException;
import org.adso.minimarket.exception.NotFoundException;
import org.adso.minimarket.mappers.CartMapper;
import org.adso.minimarket.models.*;
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
    private final ProductService productService;

    public CartServiceImpl(CartRepository cartRepository, UserRepository userRepository, CartMapper cartMapper,
                           ProductService productService) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.cartMapper = cartMapper;
        this.productService = productService;
    }

    @Override
    @Transactional
    public ShoppingCart getShoppingCart(Long userId, UUID guestId) {
        return cartMapper.toDto(getCart(userId, guestId));
    }

    @Override
    public Cart getCart(Long userId, UUID guestId) {
        Optional<Cart> cart;
        if (userId != null) {
            cart = cartRepository.findCartByUserIdAndStatus(userId, CartStatus.ACTIVE);
            if (cart.isEmpty()) {
                throw new NotFoundException("Cart not found");
            }

            return cart.get();
        }

        if (guestId != null) {
            cart = cartRepository.findCartByGuestIdAndStatus(guestId, CartStatus.ACTIVE);
            if (cart.isEmpty()) {
                throw new NotFoundException("Cart not found");
            }
            return cart.get();
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

        if (guestCart == null) return;

        for (CartItem guestItem : guestCart.getCartItems()) {
            Optional<CartItem> repeatedItem = findCartItemByProductId(userCart, guestItem.getProduct().getId());

            if (repeatedItem.isPresent()) {
                repeatedItem.get().addToQuantity(guestItem.getQuantity());
            } else {
                CartItem newItem = new CartItem(userCart, guestItem.getProduct(), guestItem.getQuantity());
                userCart.getCartItems().add(newItem);
            }
        }

        guestCart.getCartItems().clear();
        guestCart.setStatus(CartStatus.MERGED);
        guestCart.setUser(userCart.getUser());
        guestCart.setGuestId(null);
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
        return cartRepository.save(cart);
    }

    @Override
    public Cart createGuestCart() {
        return cartRepository.save(new Cart(UUID.randomUUID()));
    }

    @Override
    public Cart createGuestCart(UUID guestId) {
        if (guestId == null) {
            throw new InternalErrorException("Error on cart creation");
        }
        return cartRepository.save(new Cart(guestId));
    }

    /**
     * TODO:
     *  - Anadir upsert para hacer mas eficiente la query
     *
     *
     */
    @Override
    @Transactional
    public ShoppingCart addItemToCart(Long userId, UUID guestId, AddCartItemRequest item) {
        Cart cart = getOrCreateCart(userId, guestId);

        Product product = productService.getById(item.getProductId());
        Optional<CartItem> repeated = this.findCartItemByProductId(cart, product.getId());

        if (repeated.isPresent()) {
            repeated.get().addToQuantity(item.getQuantity());
        } else {
            CartItem newCartItem = new CartItem(cart, product, item.getQuantity());
            cart.getCartItems().add(newCartItem);
        }

        cartRepository.save(cart);
        return cartMapper.toDto(cart);
    }

    @Override
    @Transactional
    public ShoppingCart removeItemFromCart(Long userId, UUID guestId, Long productId) {
        Cart cart = getCart(userId, guestId);

        Optional<CartItem> foundItem = cart.getCartItems()
                .stream()
                .filter((item) -> item.getProduct().getId().equals(productId)).findFirst();

        foundItem.ifPresent(item -> cart.getCartItems().remove(item));

        return cartMapper.toDto(cart);
    }

    private Optional<CartItem> findCartItemByProductId(Cart cart, Long productId) {
        return cart.getCartItems()
                .stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst();
    }

    private Cart getOrCreateCart(Long userId, UUID guestId) {
        if (userId != null) {
            return cartRepository.findWithItemsByUserIdAndStatus(userId, CartStatus.ACTIVE)
                    .orElseGet(() -> createCart(userId));
        }

        if (guestId != null) {
            return cartRepository.findWithItemsByGuestIdAndStatus(guestId, CartStatus.ACTIVE)
                    .orElseGet(() -> createGuestCart(guestId));
        }

        return createGuestCart();
    }
}
