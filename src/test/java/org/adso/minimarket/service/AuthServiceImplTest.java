package org.adso.minimarket.service;


import org.adso.minimarket.dto.LoginRequest;
import org.adso.minimarket.dto.RegisterRequest;
import org.adso.minimarket.dto.AuthResponse;
import org.adso.minimarket.dto.UserResponse;
import org.adso.minimarket.exception.NotFoundException;
import org.adso.minimarket.exception.WrongCredentialsException;
import org.adso.minimarket.mappers.AuthMapper;
import org.adso.minimarket.models.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private UserService userService;

    @Mock
    private AuthMapper authMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    void register_withCorrectCredentials_itReturnsAuthUser() {
        var req = new RegisterRequest("test", "lastname", "test@gmail.com", "password123");
        var userRes = UserResponse.builder().id(1L).name("test").email("test@gmail.com").build();
        var expected = AuthResponse.builder().id(1L).name("test").email("test@gmail.com").build();

        when(userService.createUser(any())).thenReturn(userRes);
        when(authMapper.toAuthResponseDto(any(UserResponse.class))).thenReturn(expected);

        AuthResponse result = authService.register(req);

        assertEquals(expected.getEmail(), result.getEmail());
        verify(userService).createUser(any());
        verify(passwordEncoder).encode(anyString());
    }

    @Test
    void loginUser_whenValid_thenReturnsFoundUser() {
        var user = new User(1L, "test@gmail.com", "password");
        var req = new LoginRequest("test@gmail.com", "password");
        var expected = AuthResponse.builder().id(1L).email("test@gmail.com").build();

        when(userService.getUserInternalByEmail(req.email())).thenReturn(user);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(authMapper.toAuthResponseDto(user)).thenReturn(expected);

        AuthResponse result = authService.loginUser(req);

        assertEquals(expected.getId(), result.getId());
    }

    @Test
    void loginUser_whenNotFound_thenThrowsWrongCredentials() {
        when(userService.getUserInternalByEmail(anyString())).thenThrow(new NotFoundException("..."));

        assertThrows(WrongCredentialsException.class,
                () -> authService.loginUser(new LoginRequest("err@test.com", "pass")));

        verifyNoInteractions(passwordEncoder, authMapper);
    }


    @Test
    void loginUser_whenPasswordMismatch_throwsWrongCredentialsException() throws Exception {
        User usr = new User(1L, "test", "lastname", "test@gmail.com","password123");

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        when(userService.getUserInternalByEmail(any(String.class))).thenReturn(usr);

        assertThrows(WrongCredentialsException.class, () -> authService.loginUser(new LoginRequest("test@gmail.com",
                "password123")));

        verify(passwordEncoder).matches(anyString(), anyString());
        verify(userService).getUserInternalByEmail(any(String.class));
    }

}
