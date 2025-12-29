package org.adso.minimarket.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * TODO (opcional):
 *  - Cambiar el refresh a uno por httponly cookies
 *  - Adecuar el mapper
 * */
@Data
@Builder
@AllArgsConstructor
public class AuthResponse {
    @JsonProperty("acces_token")
    private String token;

    @JsonProperty("refresh_token")
    private String refreshToken;
}
