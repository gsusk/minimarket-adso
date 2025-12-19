package org.adso.minimarket.dto.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LoginRequest(
        @NotBlank(message = "required")
        @Email(message = "must be a valid email")
        String email,
        @NotBlank(message = "must not be blank")
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
