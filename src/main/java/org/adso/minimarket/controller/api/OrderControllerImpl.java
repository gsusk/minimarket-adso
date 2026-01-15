package org.adso.minimarket.controller.api;

import org.adso.minimarket.config.UserPrincipal;
import org.adso.minimarket.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderControllerImpl implements OrderController {
    public final OrderService orderService;

    public OrderControllerImpl(OrderService orderService) {
        this.orderService = orderService;
    }


    @Override
    @GetMapping("/api/orders/{order_id}")
    public ResponseEntity<?> getOrderDetails(@AuthenticationPrincipal UserPrincipal principal,
                                             @PathVariable("order_id") String orderId) {
        return ResponseEntity.ok(null);
    }
}
