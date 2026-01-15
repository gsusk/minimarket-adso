package org.adso.minimarket.service;

import org.adso.minimarket.dto.CheckoutRequest;

public interface CheckoutService {
    Object initializeCheckout(CheckoutRequest body);
}
