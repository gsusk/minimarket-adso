package org.adso.minimarket.service;

import org.adso.minimarket.controller.request.RegisterRequest;
import org.adso.minimarket.controller.request.LoginUserRequest;
import org.adso.minimarket.dto.auth.AuthResponse;
import org.adso.minimarket.exception.WrongCredentialsException;
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
    public AuthResponse createUser(RegisterRequest body) {
        User usr = new User(body.name(), body.lastName(), body.email(), this.passwordEncoder.encode(body.password()));
        return userMapper.toResponseDto(userRepository.save(usr));
    }

    @Override
    public AuthResponse loginUser(LoginUserRequest userRequest) {
        User user = userRepository.findByEmail(userRequest.email())
                .orElseThrow(() -> new WrongCredentialsException("Incorrect email or password"));

        if (!passwordEncoder.matches(userRequest.password(), user.getPassword())) {
            throw new WrongCredentialsException("Incorrect email or password");
        }

        return userMapper.toResponseDto(user);
    }
}
