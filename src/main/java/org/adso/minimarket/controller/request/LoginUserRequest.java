package org.adso.minimarket.controller.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record LoginUserRequest(
        @Email(message = "Must be a valid email")
        String email,
        @NotBlank(message = "Must not be blank")
        @Length(min = 6, message = "should have at least 6 characters")
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
