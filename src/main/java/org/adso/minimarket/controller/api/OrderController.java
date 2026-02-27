package org.adso.minimarket.controller.api;

import org.adso.minimarket.config.UserPrincipal;
import org.adso.minimarket.dto.OrderSummary;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface OrderController {
    ResponseEntity<OrderSummary> getOrderDetails(UserPrincipal principal, String orderId);
    ResponseEntity<List<OrderSummary>> getMyOrders(UserPrincipal principal);
}
