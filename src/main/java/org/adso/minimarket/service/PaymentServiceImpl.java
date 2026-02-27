package org.adso.minimarket.service;

import org.adso.minimarket.dto.CreatePaymentRequest;
import org.adso.minimarket.dto.CreatePaymentResponse;
import org.adso.minimarket.models.order.Order;
import org.adso.minimarket.models.payment.Payment;
import org.adso.minimarket.models.payment.PaymentStatus;
import org.adso.minimarket.repository.jpa.OrderRepository;
import org.adso.minimarket.repository.jpa.PaymentRepository;
import org.adso.minimarket.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public CreatePaymentResponse createPayment(CreatePaymentRequest request) {
        Order order = orderRepository.findById(request.getId())
                .orElseThrow(() -> new NotFoundException("Order not found: " + request.getId()));

        String paymentReference = "PAY-" + UUID.randomUUID().toString().toUpperCase();

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(request.getTotal());
        payment.setCurrency("usd");
        payment.setPaymentReference(paymentReference);
        payment.setStatus(PaymentStatus.COMPLETED);

        paymentRepository.save(payment);

        return CreatePaymentResponse.builder()
                .paymentReference(paymentReference)
                .currency(payment.getCurrency())
                .amount(payment.getAmount())
                .status(payment.getStatus().name().toLowerCase())
                .build();
    }
}
