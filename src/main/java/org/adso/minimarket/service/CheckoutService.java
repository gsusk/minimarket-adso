package org.adso.minimarket.service;

import org.adso.minimarket.models.user.User;

public interface CheckoutService {
    Object initializeCheckout(User user);
}
