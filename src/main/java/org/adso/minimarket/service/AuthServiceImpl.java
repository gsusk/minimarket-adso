package org.adso.minimarket.service;

import jakarta.transaction.Transactional;
import org.adso.minimarket.config.UserPrincipal;
import org.adso.minimarket.dto.AuthResponse;
import org.adso.minimarket.dto.LoginRequest;
import org.adso.minimarket.dto.RegisterRequest;
import org.adso.minimarket.exception.NotFoundException;
import org.adso.minimarket.exception.TokenInvalidException;
import org.adso.minimarket.exception.WrongCredentialsException;
import org.adso.minimarket.models.user.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

/**
 * TODO:
 * - Anadir eventos para desacoplar?
 */

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final CartService cartService;
    private final JwtService jwtService;

    AuthServiceImpl(UserService userService, PasswordEncoder passwordEncoder,
                    AuthenticationManager authManager, CartService cartService, JwtService jwtService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authManager = authManager;
        this.cartService = cartService;
        this.jwtService = jwtService;
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest req, UUID guestId) {
        User user = this.userService.createUser(RegisterRequest.builder()
                .firstName(req.firstName())
                .lastName(req.lastName())
                .email(req.email())
                .password(this.passwordEncoder.encode(req.password()))
                .build());

        UserDetails userDetails = new UserPrincipal(user);

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        if (guestId != null) {
            cartService.mergeCarts(user.getId(), guestId);
        }

        return new AuthResponse(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public AuthResponse loginUser(LoginRequest req, UUID guestId) throws NullPointerException {
        Authentication authentication;
        try {
            authentication = authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.email(), req.password())
            );
        } catch (AuthenticationException e) {
            throw new WrongCredentialsException("Invalid email or password");
        }

        UserPrincipal user = (UserPrincipal) Objects.requireNonNull(authentication.getPrincipal());

        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        if (guestId != null) {
            cartService.mergeCarts(user.getId(), guestId);
        }

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
