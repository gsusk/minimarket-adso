package org.adso.minimarket.controller.api;

import jakarta.validation.Valid;
import org.adso.minimarket.constant.AuthRoutes;
import org.adso.minimarket.dto.AuthResponse;
import org.adso.minimarket.dto.LoginRequest;
import org.adso.minimarket.dto.RefreshRequest;
import org.adso.minimarket.dto.RegisterRequest;
import org.adso.minimarket.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    public AuthControllerImpl(AuthService authService) {
        this.authService = authService;
    }

    @Override
    @PostMapping(AuthRoutes.LOGIN)
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest loginRequest,
                                              @RequestHeader(name = "X-CGuest-Id", required = false) UUID guestId) {
        return ResponseEntity.ok(this.authService.loginUser(loginRequest, guestId));
    }

    @Override
    @PostMapping(AuthRoutes.REGISTER)
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest registerRequest,
                                                 @RequestHeader(name = "X-CGuest-Id", required = false) UUID guestId) {
        return new ResponseEntity<>(this.authService.register(registerRequest, guestId), HttpStatus.CREATED);
    }

    @Override
    @PostMapping(AuthRoutes.REFRESH_TOKEN)
    public ResponseEntity<AuthResponse> auth(@RequestBody @Valid RefreshRequest refreshRequest) {
        return new ResponseEntity<>(this.authService.refresh(refreshRequest.getRefreshToken()), HttpStatus.OK);
    }
}
