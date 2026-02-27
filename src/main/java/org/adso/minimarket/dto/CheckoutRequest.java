package org.adso.minimarket.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckoutRequest {
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
