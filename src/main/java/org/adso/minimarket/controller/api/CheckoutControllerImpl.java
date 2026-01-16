package org.adso.minimarket.controller.api;

import org.adso.minimarket.config.UserPrincipal;
import org.adso.minimarket.dto.CreatePaymentRequest;
import org.adso.minimarket.service.CheckoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckoutControllerImpl implements CheckoutController {
    private final CheckoutService checkoutService;

    public CheckoutControllerImpl(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @Override
    @PostMapping("/checkout/initialize")
    public ResponseEntity<?> initializeCheckout(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody(required = false) CreatePaymentRequest body) {

        return ResponseEntity.ok(checkoutService.initializeCheckout(userPrincipal.getUser()));
    }
}
