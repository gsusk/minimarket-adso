package org.adso.minimarket.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DetailedUser {
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNUmber;
}
