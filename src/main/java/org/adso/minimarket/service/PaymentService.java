package org.adso.minimarket.service;

import org.adso.minimarket.dto.CreatePaymentRequest;
import org.adso.minimarket.dto.CreatePaymentResponse;

public interface PaymentService {
    CreatePaymentResponse createPayment(CreatePaymentRequest request);
}
