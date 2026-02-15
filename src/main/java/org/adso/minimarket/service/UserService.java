package org.adso.minimarket.service;

import org.adso.minimarket.dto.BasicUser;
import org.adso.minimarket.dto.DetailedUser;
import org.adso.minimarket.dto.RegisterRequest;
import org.adso.minimarket.dto.UserUpdateRequest;
import org.adso.minimarket.models.user.User;

public interface UserService {
    User createUser(RegisterRequest registerRequest);

    BasicUser getUserByEmail(String email);

    BasicUser getBasicUserById(Long id);

    DetailedUser updateUserProfile(UserUpdateRequest dto, Long id);

    User getUserById(Long id);

    User getUserInternalByEmail(String email);
}