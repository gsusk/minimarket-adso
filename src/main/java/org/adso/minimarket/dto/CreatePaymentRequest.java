package org.adso.minimarket.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatePaymentRequest {
    @JsonProperty("orderId")
    private UUID id;
    private String email;
    private Long userId;
    private String status;
    private BigDecimal total;
    private LocalDateTime createdAt;
    @NotBlank
    private String shippingFullName;
    @NotBlank
    private String shippingAddressLine;
    @NotBlank
    private String shippingCity;
    @NotBlank
    private String shippingZipCode;
    @NotBlank
    private String shippingCountry;
}
