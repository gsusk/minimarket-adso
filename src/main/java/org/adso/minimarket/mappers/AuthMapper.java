package org.adso.minimarket.mappers;

import org.adso.minimarket.dto.AuthResponse;
import org.adso.minimarket.dto.UserResponse;
import org.adso.minimarket.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AuthMapper{
    AuthResponse toAuthResponseDto(UserResponse userResponse);
    @Mapping(target = "token", source = "token")
    AuthResponse toAuthResponseDto(User user, String token);
    AuthResponse toAuthResponseDto(User user);
}
