package org.adso.minimarket.controller.api;

import org.adso.minimarket.dto.CheckoutRequest;
import org.adso.minimarket.service.CheckoutService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CheckoutControllerImpl implements CheckoutController {
    private final CheckoutService checkoutService;

    public CheckoutControllerImpl(CheckoutService checkoutService) {
        this.checkoutService = checkoutService;
    }

    @Override
    public ResponseEntity<?> initializeCheckout(@RequestBody CheckoutRequest body) {
        return ResponseEntity.ok(checkoutService.initializeCheckout(body));
    }
}
