package org.adso.minimarket.service;

import org.adso.minimarket.controller.request.RegisterRequest;
import org.adso.minimarket.controller.request.LoginUserRequest;
import org.adso.minimarket.dto.auth.AuthResponse;
import org.adso.minimarket.exception.WrongCredentialsException;
import org.adso.minimarket.mappers.UserMapper;
import org.adso.minimarket.models.User;
import org.adso.minimarket.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {


    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {

        @Bean
        public UserService userService(UserRepository userRepository, PasswordEncoder pe, UserMapper userMapper) {
            return new UserServiceImpl(userRepository, pe, userMapper);
        }
    }

    @MockitoBean
    private  PasswordEncoder passwordEncoder;

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    public UserService userService;

    @MockitoBean
    public UserMapper userMapper;

    @Test
    void whenServiceCalledWithValid_thenReturnsNewUser() throws Exception {
        RegisterRequest req = new RegisterRequest("jorge", "contreras", "validemail@gmail.com", "password123");
        User mockUsr = new User(1L, req.name(), req.lastName(), req.email(), req.password());
        AuthResponse dto = AuthResponse.builder()
                .id(mockUsr.getId())
                .name(mockUsr.getName())
                .email(mockUsr.getEmail())
                .build();

        when(userRepository.save(any(User.class))).thenReturn(mockUsr);
        when(userMapper.toResponseDto(any(User.class))).thenReturn(dto);

        AuthResponse got = userService.createUser(req);

        assertEquals("jorge", got.getName());
        assertEquals(1L, got.getId());
        assertEquals("validemail@gmail.com", got.getEmail());
    }

    @Test
    void whenLoginUserCalled_thenReturnsFoundUser() throws Exception {
        User mockUsr = new User(1L, "test", "testLastName", "test@gmail.com", "password123");
        LoginUserRequest req = new LoginUserRequest(mockUsr.getEmail(), mockUsr.getPassword());
        AuthResponse dto = AuthResponse.builder()
                .name(mockUsr.getName())
                .email(mockUsr.getEmail())
                .id(mockUsr.getId())
                .build();

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(mockUsr));
        when(userMapper.toResponseDto(any(User.class))).thenReturn(dto);
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        AuthResponse got = userService.loginUser(req);

        assertEquals("test", got.getName());
        assertEquals("test@gmail.com", got.getEmail());
        assertEquals(1L, got.getId());

        verify(userRepository).findByEmail(any(String.class));
        verify(passwordEncoder).matches(anyString(), anyString());
    }

    @Test
    void throwsBadCredentialsException_whenEmailNotFound() throws Exception {
        LoginUserRequest req = new LoginUserRequest("test", "test@gmail.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(WrongCredentialsException.class, () -> userService.loginUser(req));

        verify(userRepository).findByEmail(any(String.class));
    }

    @Test
    void throwsBadCredentialsException_whenPasswordIsIncorrect() throws Exception {
        LoginUserRequest req = new LoginUserRequest("test", "test@gmail.com");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(new User("test", "testLastName", "test@gmail.com", "password123")));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertThrows(WrongCredentialsException.class, () -> userService.loginUser(req));

        verify(userRepository).findByEmail(any(String.class));
        verify(passwordEncoder).matches(anyString(), anyString());
    }
}
