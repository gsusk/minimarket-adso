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
    @JsonProperty("order_id")
    private UUID id;
    private String email;
    @JsonProperty("user_id")
    private Long userId;
    private String status;
    private BigDecimal total;
    private LocalDateTime createdAt;
    @NotBlank
    @JsonProperty("shipping_name")
    private String shippingFullName;
    @NotBlank
    @JsonProperty("shipping_address")
    private String shippingAddressLine;
    @NotBlank
    @JsonProperty("shipping_city")
    private String shippingCity;
    @NotBlank
    @JsonProperty("shipping_zipcode")
    private String shippingZipCode;
    @NotBlank
    @JsonProperty("shipping_country")
    private String shippingCountry;
}
