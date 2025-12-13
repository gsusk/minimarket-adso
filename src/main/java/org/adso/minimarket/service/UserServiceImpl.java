package org.adso.minimarket.service;

import org.adso.minimarket.controller.request.CreateUserRequest;
import org.adso.minimarket.dto.UserDto;
import org.adso.minimarket.mappers.UserMapper;
import org.adso.minimarket.models.User;
import org.adso.minimarket.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }


    @Override
    public UserDto createUser(CreateUserRequest body) {
        User usr = new User(body.name(), body.lastName(), body.email(), this.passwordEncoder.encode(body.password()));
        return userMapper.toDto(userRepository.save(usr));
    }
}
