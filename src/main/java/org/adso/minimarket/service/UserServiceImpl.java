package org.adso.minimarket.service;

import org.adso.minimarket.dto.request.RegisterRequest;
import org.adso.minimarket.dto.response.UserResponse;
import org.adso.minimarket.exception.NotFoundException;
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
                .orElseThrow(() -> new NotFoundException("User not found"));

        return userMapper.toResponseDto(usr);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User usr = this.userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User Not Found"));

        return userMapper.toResponseDto(usr);
    }

    @Override
    public User getUserInternalByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
}
