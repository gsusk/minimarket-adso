package org.adso.minimarket.controller.api;

import jakarta.validation.Valid;
import org.adso.minimarket.config.UserPrincipal;
import org.adso.minimarket.dto.CreatePaymentRequest;
import org.adso.minimarket.dto.CreatePaymentResponse;
import org.springframework.http.ResponseEntity;

public interface CheckoutController {
    ResponseEntity<CreatePaymentResponse> processCheckout(UserPrincipal userPrincipal, CreatePaymentRequest body);
}
