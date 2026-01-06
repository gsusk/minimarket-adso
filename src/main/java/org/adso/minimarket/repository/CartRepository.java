package org.adso.minimarket.repository;

import org.adso.minimarket.models.Cart;
import org.adso.minimarket.models.CartStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findCartByUserIdAndStatus(Long userId, CartStatus status);

    Optional<Cart> findCartByGuestIdAndStatus(UUID guestId, CartStatus status);

    List<Cart> findCartsByUserId(Long userId);

    @EntityGraph(attributePaths = {"cartItem", "product"})
    Optional<Cart> findShoppingCartByUserIdAndStatus(Long userId, CartStatus cartStatus);

    @EntityGraph(attributePaths = {"cartItem", "product"})
    Optional<Cart> findShoppingCartByGuestIdAndStatus(UUID guestId, CartStatus cartStatus);
}
