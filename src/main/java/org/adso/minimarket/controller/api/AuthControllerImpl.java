package org.adso.minimarket.controller.api;

import org.adso.minimarket.constant.AuthRoutes;
import org.adso.minimarket.dto.request.LoginRequest;
import org.adso.minimarket.dto.request.RegisterRequest;
import org.adso.minimarket.dto.response.AuthResponse;
import org.adso.minimarket.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthControllerImpl implements AuthController {

    private final AuthService authService;

    public AuthControllerImpl(AuthService authService) {
        this.authService = authService;
    }

    @Override
    @PostMapping(AuthRoutes.LOGIN)
    public ResponseEntity<AuthResponse> login(LoginRequest loginRequest) {
        return ResponseEntity.ok(this.authService.loginUser(loginRequest));
    }

    @Override
    @PostMapping(AuthRoutes.REGISTER)
    public ResponseEntity<AuthResponse> register(RegisterRequest registerRequest) {
        return new ResponseEntity<>(this.authService.register(registerRequest), HttpStatus.CREATED);
    }

}
