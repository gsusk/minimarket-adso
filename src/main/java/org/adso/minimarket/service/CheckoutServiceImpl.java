package org.adso.minimarket.service;

import com.stripe.exception.StripeException;
import org.adso.minimarket.dto.CreatePaymentRequest;
import org.adso.minimarket.dto.CreatePaymentResponse;
import org.adso.minimarket.dto.OrderDetails;
import org.adso.minimarket.exception.InternalErrorException;
import org.adso.minimarket.models.user.User;
import org.springframework.stereotype.Service;

@Service
public class CheckoutServiceImpl implements CheckoutService {
    private final OrderService orderService;
    private final PaymentService paymentService;

    public CheckoutServiceImpl(OrderService orderService, PaymentServiceImpl paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
    }

    @Override
    public CreatePaymentResponse initializeCheckout(User user) {
        OrderDetails order = orderService.placeOrder(user);
        try {
            return paymentService.createPayment(
                    CreatePaymentRequest.builder()
                            .id(order.getId())
                            .email(order.getEmail())
                            .total(order.getTotal())
                            .status(order.getStatus())
                            .userId(order.getUserId())
                            .build()
            );
        } catch (StripeException e) {
            throw new InternalErrorException(e.getMessage());
        }
    }
}
