package org.adso.minimarket.service;

import com.stripe.exception.StripeException;
import org.adso.minimarket.dto.CreatePaymentRequest;
import org.adso.minimarket.dto.CreatePaymentResponse;

public interface PaymentService {
    public CreatePaymentResponse createPayment(CreatePaymentRequest request) throws StripeException;
}
