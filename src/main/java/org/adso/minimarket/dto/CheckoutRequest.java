package org.adso.minimarket.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutRequest {
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
