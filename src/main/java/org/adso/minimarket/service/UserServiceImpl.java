package org.adso.minimarket.service;

import org.adso.minimarket.controller.request.CreateUserRequest;
import org.adso.minimarket.controller.request.LoginUserRequest;
import org.adso.minimarket.dto.UserResponseDto;
import org.adso.minimarket.exception.BadAuthCredentialsException;
import org.adso.minimarket.mappers.UserMapper;
import org.adso.minimarket.models.User;
import org.adso.minimarket.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }


    @Override
    public UserResponseDto createUser(CreateUserRequest body) {
        User usr = new User(body.name(), body.lastName(), body.email(), this.passwordEncoder.encode(body.password()));
        return userMapper.toResponseDto(userRepository.save(usr));
    }

    @Override
    public UserResponseDto loginUser(LoginUserRequest userRequest) {
        User user = userRepository.findByEmail(userRequest.email())
                .orElseThrow(() -> new BadAuthCredentialsException("Incorrect email or password"));

        if (!passwordEncoder.matches(userRequest.password(), user.getPassword())) {
            throw new BadAuthCredentialsException("Incorrect email or password");
        }

        return userMapper.toResponseDto(user);
    }
}
