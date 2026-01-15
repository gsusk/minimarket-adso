package org.adso.minimarket.service;

import org.adso.minimarket.dto.CheckoutRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CheckoutServiceImpl implements CheckoutService {
    @Override
    @Transactional
    public Object initializeCheckout(CheckoutRequest body) {
        return null;
    }
}
