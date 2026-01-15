package org.adso.minimarket.repository;

import org.adso.minimarket.models.Order;
import org.adso.minimarket.models.OrderStatus;
import org.adso.minimarket.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Optional<Order> findFirstByUserAndStatusOrderByCreatedAtDesc(User user, OrderStatus status);
}
