package org.adso.minimarket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePaymentResponse {
    @JsonProperty("client_secret")
    private String clientSecret;
    @JsonProperty("payment_intent_id")
    private String paymentIntentId;
    private String currency;
    private Long amount;
    private String status;
}