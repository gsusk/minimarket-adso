package org.adso.minimarket.mappers;

import org.adso.minimarket.dto.BasicUser;
import org.adso.minimarket.models.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    BasicUser toResponseDto(User user);
}
