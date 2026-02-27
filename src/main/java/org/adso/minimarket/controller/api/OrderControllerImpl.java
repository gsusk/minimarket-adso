package org.adso.minimarket.controller.api;

import org.adso.minimarket.config.UserPrincipal;
import org.adso.minimarket.dto.OrderSummary;
import org.adso.minimarket.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class OrderControllerImpl implements OrderController {
    private final OrderService orderService;

    public OrderControllerImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    @GetMapping("/orders/{order_id}")
    public ResponseEntity<OrderSummary> getOrderDetails(
            @AuthenticationPrincipal UserPrincipal principal,
            @PathVariable("order_id") String orderId) {

        OrderSummary summary = orderService.getOrderById(UUID.fromString(orderId), principal.getId());
        return ResponseEntity.ok(summary);
    }

    @Override
    @GetMapping("/orders")
    public ResponseEntity<List<OrderSummary>> getMyOrders(
            @AuthenticationPrincipal UserPrincipal principal) {

        return ResponseEntity.ok(orderService.getUserOrders(principal.getId()));
    }
}
