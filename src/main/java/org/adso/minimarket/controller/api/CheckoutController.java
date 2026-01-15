package org.adso.minimarket.controller.api;

import org.adso.minimarket.dto.CheckoutRequest;
import org.springframework.http.ResponseEntity;

public interface CheckoutController {
    ResponseEntity<?> initializeCheckout(CheckoutRequest body);
}
