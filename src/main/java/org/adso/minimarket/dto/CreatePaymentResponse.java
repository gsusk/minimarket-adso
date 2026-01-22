package org.adso.minimarket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePaymentResponse {
    private String clientSecret;
    private String paymentIntentId;
    private String currency;
    private Long amount;
    private String status;
}