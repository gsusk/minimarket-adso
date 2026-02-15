package org.adso.minimarket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DetailedUser {
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String phoneNumber;
}
