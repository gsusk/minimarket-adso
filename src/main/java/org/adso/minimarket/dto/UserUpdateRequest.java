package org.adso.minimarket.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserUpdateRequest {
    @Size(min = 1, max = 255, message = "too long")
    private String firstName;
    @Size(min = 1, message = "required")
    private String lastName;
    @Size(max = 300, message = "too long")
    private String address;
    @Size(min = 6)
    private String phoneNumber;
}
