package org.adso.minimarket.service;

import org.adso.minimarket.controller.request.CreateUserRequest;
import org.adso.minimarket.dto.UserDto;
import org.adso.minimarket.models.User;
import org.adso.minimarket.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(CreateUserRequest body) {
        User usr = new User(1L, body.name(), body.lastName(), body.email(), body.password());
        User saved = userRepository.save(usr);
        return new UserDto(
                saved.getId(),
                saved.getName(),
                saved.getLastName(),
                saved.getEmail()
        );
    }
}
