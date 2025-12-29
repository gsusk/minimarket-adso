package org.adso.minimarket.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefreshRequest {
    @JsonProperty("refresh_token")
    @NotBlank
    @Size(min = 10, max = 2048, message = "Refresh token malformed or too long")
    private String refreshToken;
}
