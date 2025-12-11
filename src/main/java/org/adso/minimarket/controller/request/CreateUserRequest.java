package org.adso.minimarket.controller.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
        String email,

        @Size(min = 6)
        @NotBlank
        String password
) {
    @Override
    public String name() {
        return name;
    }

    @Override
    public String lastName() {
        return lastName;
    }

    @Override
    public String email() {
        return email;
    }

    @Override
    public String password() {
        return password;
    }
}

