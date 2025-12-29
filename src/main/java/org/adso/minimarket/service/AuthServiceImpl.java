package org.adso.minimarket.service;

import org.adso.minimarket.config.UserPrincipal;
import org.adso.minimarket.dto.AuthResponse;
import org.adso.minimarket.dto.LoginRequest;
import org.adso.minimarket.dto.RegisterRequest;
import org.adso.minimarket.exception.TokenInvalidException;
import org.adso.minimarket.models.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    AuthServiceImpl(UserService userService, PasswordEncoder passwordEncoder,
                    AuthenticationManager authManager, JwtService jwtService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.jwtService = jwtService;
    }

    @Override
    public AuthResponse register(RegisterRequest req) {
        User user = this.userService.createUser(RegisterRequest.builder()
                .name(req.name())
                .lastName(req.lastName())
                .email(req.email())
                .password(this.passwordEncoder.encode(req.password()))
                .build());

        UserDetails userDetails = new UserPrincipal(user);

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public AuthResponse loginUser(LoginRequest req) throws NullPointerException {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password())
        );

        UserPrincipal user = (UserPrincipal) Objects.requireNonNull(authentication.getPrincipal());

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    public AuthResponse refresh(String refreshToken) {
        String email = jwtService.extractRefreshUsername(refreshToken);

        if (email == null) {
            throw new TokenInvalidException("Unauthorized");
        }

        User user = userService.getUserInternalByEmail(email);
        UserDetails userDetails = new UserPrincipal(user);

        if (!jwtService.isRefreshTokenValid(refreshToken, userDetails)) {
            throw new TokenInvalidException("Unauthorized");
        }

        final String newAccessToken = jwtService.generateAccessToken(userDetails);

        return new AuthResponse(newAccessToken, null);
    }
}
