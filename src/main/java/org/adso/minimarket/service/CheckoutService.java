package org.adso.minimarket.service;

import org.adso.minimarket.dto.CheckoutRequest;
import org.adso.minimarket.dto.CreatePaymentResponse;
import org.adso.minimarket.models.user.User;

public interface CheckoutService {
    CreatePaymentResponse processCheckout(User user, CheckoutRequest request);
}
