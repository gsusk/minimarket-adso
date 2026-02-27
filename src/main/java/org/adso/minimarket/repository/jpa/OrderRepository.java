package org.adso.minimarket.repository.jpa;

import org.adso.minimarket.models.order.Order;
import org.adso.minimarket.models.order.OrderStatus;
import org.adso.minimarket.models.user.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    @EntityGraph(attributePaths = {"user", "orderItems"})
    Optional<Order> findOrderByIdAndUserId(UUID id, Long userId);
}
