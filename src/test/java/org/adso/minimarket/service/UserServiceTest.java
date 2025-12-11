package org.adso.minimarket.service;

import org.adso.minimarket.controller.request.CreateUserRequest;
import org.adso.minimarket.dto.UserDto;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class UserServiceTest {


    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {

        @Bean
        public UserService userService(UserRepository userRepository) {
            return new UserServiceImpl(userRepository);
        }
    }

    @MockitoBean
    private UserRepository userRepository;

    @Autowired
    public UserService userService;

    @Test
    void whenServiceCalledWithValid_thenReturnsNewUser() throws Exception {
        CreateUserRequest req = new CreateUserRequest("jorge", "contreras", "validemail@gmail.com", "password123");
        User saved = new User( 1L, req.name(), req.lastName(), req.email(), req.password());

        when(userRepository.save(any(User.class))).thenReturn(saved);

        UserDto got = userService.createUser(req);

        assertEquals("jorge", got.getName());
        assertEquals("contreras", got.getLastname());
        assertEquals("validemail@gmail.com", got.getEmail());
    }
}
