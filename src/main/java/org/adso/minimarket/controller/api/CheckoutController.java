package org.adso.minimarket.controller.api;

import org.adso.minimarket.config.UserPrincipal;
import org.adso.minimarket.dto.CreatePaymentRequest;
import org.springframework.http.ResponseEntity;

public interface CheckoutController {
    ResponseEntity<?> initializeCheckout(UserPrincipal userPrincipal, CreatePaymentRequest body);
}
