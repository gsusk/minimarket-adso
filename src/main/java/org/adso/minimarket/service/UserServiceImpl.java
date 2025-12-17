package org.adso.minimarket.service;

import org.adso.minimarket.dto.request.RegisterRequest;
import org.adso.minimarket.dto.response.UserResponse;
import org.adso.minimarket.exception.WrongCredentialsException;
import org.adso.minimarket.mappers.UserMapper;
import org.adso.minimarket.models.User;
import org.adso.minimarket.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponse createUser(RegisterRequest body) {
        User usr = new User(body.name(), body.lastName(), body.email(), body.password());
        return userMapper.toResponseDto(userRepository.save(usr));
    }

    @Override
    public UserResponse getUserByEmail(String email) {
        User usr = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new WrongCredentialsException("Invalid email or password"));

        return userMapper.toResponseDto(usr);
    }

}
