package org.adso.minimarket.service;

import org.adso.minimarket.controller.request.CreateUserRequest;
import org.adso.minimarket.controller.request.LoginUserRequest;
import org.adso.minimarket.dto.UserDto;
import org.adso.minimarket.mappers.UserMapper;
import org.adso.minimarket.models.User;
import org.adso.minimarket.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class UserServiceTest {


    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {

        @Bean
        public UserService userService(UserRepository userRepository, PasswordEncoder pe, UserMapper userMapper) {
            return new UserServiceImpl(userRepository, pe, userMapper);
        }

        @Bean
        protected PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder(10);
        }


    }

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    public UserService userService;

    @MockitoBean
    public UserMapper userMapper;

    @Test
    void whenServiceCalledWithValid_thenReturnsNewUser() throws Exception {
        CreateUserRequest req = new CreateUserRequest("jorge", "contreras", "validemail@gmail.com", "password123");
        User mockUsr = new User(1L, req.name(), req.lastName(), req.email(), req.password());
        UserDto dto = UserDto.builder()
                .id(mockUsr.getId())
                .name(mockUsr.getName())
                .email(mockUsr.getEmail())
                .build();

        when(userRepository.save(any(User.class))).thenReturn(mockUsr);
        when(userMapper.toDto(any(User.class))).thenReturn(dto);

        UserDto got = userService.createUser(req);

        assertEquals("jorge", got.getName());
        assertEquals(1L, got.getId());
        assertEquals("validemail@gmail.com", got.getEmail());

    }

    @Test
    void whenServiceFindByEmailCalled_thenReturnsFoundUser() throws Exception {
        User mockUsr = new User(1L, "test", "testLastName", "test@gmail.com", "password123");
        LoginUserRequest req = new LoginUserRequest(mockUsr.getEmail(), mockUsr.getPassword());
        UserDto dto = UserDto.builder()
                .name(mockUsr.getName())
                .email(mockUsr.getEmail())
                .id(mockUsr.getId())
                .build();

        when(userRepository.findByEmail(any(String.class))).thenReturn(mockUsr);
        when(userMapper.toDto(any(User.class))).thenReturn(dto);

        UserDto got = userService.findByEmail(req);

        assertEquals("test", got.getName());
        assertEquals("test@gmail.com", got.getEmail());
        assertEquals(1L, got.getId());
    }
}
