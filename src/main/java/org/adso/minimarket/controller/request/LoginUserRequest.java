package org.adso.minimarket.controller.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record LoginUserRequest(
        @Email
        String email,
        @NotBlank
        @Length(min = 6, message = "Must be a valid email")
        String password
) {
    @Override
    public String email() {
        return email;
    }

    @Override
    public String password() {
        return password;
    }
}
