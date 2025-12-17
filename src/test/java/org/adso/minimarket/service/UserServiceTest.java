package org.adso.minimarket.service;

import org.adso.minimarket.dto.request.RegisterRequest;
import org.adso.minimarket.dto.response.UserResponse;
import org.adso.minimarket.exception.WrongCredentialsException;
import org.adso.minimarket.mappers.UserMapper;
import org.adso.minimarket.models.User;
import org.adso.minimarket.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {


    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {

        @Bean
        public UserService userService(UserRepository userRepository, UserMapper userMapper) {
            return new UserServiceImpl(userRepository, userMapper);
        }
    }

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    public UserService userService;

    @MockitoBean
    public UserMapper userMapper;

    @Test
    void whenServiceCreateCalledWithValid_thenReturnsNewUser() throws Exception {
        RegisterRequest req = new RegisterRequest("jorge", "contreras", "validemail@gmail.com", "password123");
        User mockUsr = new User(1L, req.name(), req.lastName(), req.email(), req.password());
        UserResponse dto = UserResponse.builder()
                .id(mockUsr.getId())
                .name(mockUsr.getName())
                .email(mockUsr.getEmail())
                .build();

        when(userRepository.save(any(User.class))).thenReturn(mockUsr);
        when(userMapper.toResponseDto(any(User.class))).thenReturn(dto);

        UserResponse got = userService.createUser(req);

        assertEquals("jorge", got.getName());
        assertEquals(1L, got.getId());
        assertEquals("validemail@gmail.com", got.getEmail());
    }

    @Test
    void whenServiceGetByEmail_thenReturnsUser() throws Exception {
        User usr = new User(1L, "test", "lastname", "test@gmail.com", "password123");
        UserResponse ur = UserResponse.builder()
                .id(usr.getId())
                .name(usr.getName())
                .email(usr.getEmail())
                .password(usr.getPassword())
                .lastName(usr.getLastName())
                .build();

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.of(usr));
        when(userMapper.toResponseDto(any(User.class))).thenReturn(ur);

        UserResponse got = userService.getUserByEmail(usr.getEmail());

        assertEquals(usr.getEmail(), got.getEmail());
        assertEquals(usr.getId(), got.getId());
        assertEquals(usr.getName(), got.getName());
        assertEquals(usr.getLastName(), got.getLastName());

        verify(userRepository).findByEmail(usr.getEmail());
        verify(userMapper).toResponseDto(usr);
    }


    @Test
    void throwsBadCredentialsException_whenEmailNotFound() throws Exception {
        String email = "nonexistant@gmail.com";

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        assertThrows(WrongCredentialsException.class, () -> userService.getUserByEmail(email));

        verify(userRepository).findByEmail(any(String.class));
        verifyNoInteractions(userMapper);
    }
}
