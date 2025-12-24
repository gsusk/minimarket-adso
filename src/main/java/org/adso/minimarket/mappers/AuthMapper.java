package org.adso.minimarket.mappers;

import org.adso.minimarket.dto.AuthResponse;
import org.adso.minimarket.dto.UserResponse;
import org.adso.minimarket.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthMapper{
    AuthResponse toAuthResponseDto(UserResponse userResponse);
    AuthResponse toAuthResponseDto(User user);
}
