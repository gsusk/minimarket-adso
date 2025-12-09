package org.adso.minimarket.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreateUserRequest(
        @NotBlank
        @Size(max = 255)
        String name,

        @NotBlank
        @JsonProperty("last_name")
        String lastName,

        @Email
        @NotBlank
        String email,

        @Size(min = 6)
        @NotBlank
        String password
) {
}

