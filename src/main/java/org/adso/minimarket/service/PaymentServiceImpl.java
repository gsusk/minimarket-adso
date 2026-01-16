package org.adso.minimarket.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.adso.minimarket.dto.CreatePaymentRequest;
import org.adso.minimarket.dto.CreatePaymentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${application.security.stripe.secret_key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public CreatePaymentResponse createPayment(CreatePaymentRequest request) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(totalToCents(request.getTotal()))
                .setCurrency("usd")
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods
                                .builder()
                                .setEnabled(true)
                                .build()
                )
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        return convertToPaymentResponse(paymentIntent);
    }

    private Long totalToCents(BigDecimal amount) {
        return amount.movePointRight(2).longValueExact();
    }

    private CreatePaymentResponse convertToPaymentResponse(PaymentIntent paymentIntent) {
        return CreatePaymentResponse.builder()
                .amount(paymentIntent.getAmount())
                .status(paymentIntent.getStatus())
                .paymentIntentId(paymentIntent.getId())
                .clientSecret(paymentIntent.getClientSecret())
                .currency(paymentIntent.getCurrency())
                .build();
    }
}
