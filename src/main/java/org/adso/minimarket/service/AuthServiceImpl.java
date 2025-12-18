package org.adso.minimarket.service;

import org.adso.minimarket.dto.request.LoginRequest;
import org.adso.minimarket.dto.request.RegisterRequest;
import org.adso.minimarket.dto.response.AuthResponse;
import org.adso.minimarket.dto.response.UserResponse;
import org.adso.minimarket.exception.NotFoundException;
import org.adso.minimarket.exception.WrongCredentialsException;
import org.adso.minimarket.mappers.AuthMapper;
import org.adso.minimarket.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final AuthMapper authMapper;
    private final PasswordEncoder passwordEncoder;

    AuthServiceImpl(UserService userService, AuthMapper authMapper, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.authMapper = authMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AuthResponse register(RegisterRequest rr) {
        UserResponse user = this.userService.createUser(RegisterRequest.builder()
                .name(rr.name())
                .lastName(rr.lastName())
                .email(rr.email())
                .password(this.passwordEncoder.encode(rr.password()))
                .build());

        return authMapper.toAuthResponseDto(user);
    }

    @Override
    public AuthResponse loginUser(LoginRequest req) {
        User user;
        try {
            user = this.userService.getUserInternalByEmail(req.email());
        } catch (NotFoundException e) {
            throw new WrongCredentialsException("Invalid email or password");
        }

        if (!this.passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new WrongCredentialsException("Invalid email or password");
        }

        return authMapper.toAuthResponseDto(user);
    }
}
