package org.adso.minimarket.mappers;

import org.adso.minimarket.dto.UserDto;
import org.adso.minimarket.dto.auth.AuthResponse;
import org.adso.minimarket.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<UserDto, User> {
    AuthResponse toResponseDto(User user);
}
