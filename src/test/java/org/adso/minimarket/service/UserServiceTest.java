package org.adso.minimarket.service;

import org.adso.minimarket.dto.request.RegisterRequest;
import org.adso.minimarket.dto.response.UserResponse;
import org.adso.minimarket.exception.NotFoundException;
import org.adso.minimarket.mappers.UserMapper;
import org.adso.minimarket.models.User;
import org.adso.minimarket.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
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
    void create_withValid_thenReturnsNewUser() throws Exception {
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
    void getUserByEmail_withValid_thenReturnsUser() throws Exception {
        User usr = new User(1L, "test", "lastname", "test@gmail.com", "password123");
        UserResponse ur = UserResponse.builder()
                .id(usr.getId())
                .name(usr.getName())
                .email(usr.getEmail())
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
    void getUserByEmail_whenEmailNotFound_throwsBadCredentialsException() throws Exception {
        String email = "nonexistant@gmail.com";

        when(userRepository.findByEmail(any(String.class))).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserByEmail(email));

        verifyNoInteractions(userMapper);
    }

    @Test
    void getUserById_withValidId_thenReturnsUser() throws Exception {
        Long id = 1L;
        User usr = new User();
        UserResponse ur = UserResponse.builder()
                .id(id)
                .name("test")
                .email("test@gmail.com")
                .build();

        when(userRepository.findById(id)).thenReturn(Optional.of(usr));
        when(userMapper.toResponseDto(any(User.class))).thenReturn(ur);

        UserResponse got = userService.getUserById(id);

        assertEquals(ur.getId(), got.getId());
        assertEquals(ur.getEmail(), got.getEmail());
        assertEquals(ur.getLastName(), got.getLastName());
        assertEquals(ur.getName(), got.getName());

        verify(userRepository).findById(any(Long.class));
        verify(userMapper).toResponseDto(usr);
    }

    @Test
    void getUserById_UserNotFound_thenThrowException() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserById(1L));

        verify(userRepository).findById(anyLong());
        verifyNoInteractions(userMapper);
    }

    @Test
    void getUserInternalByEmail_thenReturnUserEntity() {
        String email = "test@gmail.com";

        User user = new User("Test", "User", email, "pass");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User result = userService.getUserInternalByEmail(email);

        assertEquals(email, result.getEmail());

        verify(userRepository).findByEmail(email);
        verifyNoInteractions(userMapper);
    }

    @Test
    void getUserInternalByEmail_UserNotFound_thenThrowException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.getUserInternalByEmail("missing@gmail.com"));

        verify(userRepository).findByEmail(anyString());
        verifyNoInteractions(userMapper);
    }
}
