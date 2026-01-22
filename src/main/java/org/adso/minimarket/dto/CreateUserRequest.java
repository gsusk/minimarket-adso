package org.adso.minimarket.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUserRequest {
    @NotBlank(message = "required")
    @Size(max = 255, message = "too long")
    @JsonProperty("first_name")
    String firstName;

    @NotBlank(message = "required")
    @JsonProperty("last_name")
    String lastName;

    @Email(message = "must be a valid email")
    @NotBlank(message = "required")
    String email;

    @Size(min = 6, message = "should have at least 6 characters")
    @NotBlank(message = "required")
    String password;
}
