package org.adso.minimarket.service;

import org.adso.minimarket.dto.CreatePaymentRequest;
import org.adso.minimarket.dto.CreatePaymentResponse;

import java.util.UUID;

public interface PaymentService {
    CreatePaymentResponse createPayment(CreatePaymentRequest request);
    CreatePaymentResponse getPaymentByOrderId(UUID orderId, Long userId);
}
