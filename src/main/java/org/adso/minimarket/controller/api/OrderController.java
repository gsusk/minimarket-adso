package org.adso.minimarket.controller.api;

import org.adso.minimarket.config.UserPrincipal;
import org.springframework.http.ResponseEntity;

public interface OrderController {
    ResponseEntity<?> getOrderDetails(UserPrincipal principal, String orderId);
}
