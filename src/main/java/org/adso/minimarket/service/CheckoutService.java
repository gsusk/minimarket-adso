package org.adso.minimarket.service;

import org.adso.minimarket.dto.CreatePaymentRequest;
import org.adso.minimarket.dto.CreatePaymentResponse;
import org.adso.minimarket.models.user.User;

public interface CheckoutService {
    CreatePaymentResponse processCheckout(User user, CreatePaymentRequest request);
}
