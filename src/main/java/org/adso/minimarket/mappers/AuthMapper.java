package org.adso.minimarket.mappers;

import org.adso.minimarket.dto.response.AuthResponse;
import org.adso.minimarket.dto.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper{
    AuthResponse toAuthResponseDto(UserResponse userResponse);
}
