package org.adso.minimarket.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * TODO (opcional):
 *  - Cambiar el refresh a uno por httponly cookies
 *  - Adecuar el mapper
 *
 */
@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthResponse {
    @JsonProperty("access_token")
    private String token;

    @JsonIgnore
    private String refreshToken;
}
