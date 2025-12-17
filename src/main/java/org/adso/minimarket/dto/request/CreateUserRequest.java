package org.adso.minimarket.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserRequest {
    @NotBlank(message = "required")
    @Size(max = 255, message = "too long")
    String name;

    @NotBlank(message = "required")
    @JsonProperty("last_name")
    String lastName;

    @Email(message = "Must be a valid email")
    String email;

    @Size(min = 6, message = "should have at least 6 characters")
    @NotBlank(message = "required")
    String password;
}
