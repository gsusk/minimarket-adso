package org.adso.minimarket.service;

import org.adso.minimarket.dto.BasicUser;
import org.adso.minimarket.dto.DetailedUser;
import org.adso.minimarket.dto.RegisterRequest;
import org.adso.minimarket.dto.UserUpdateRequest;
import org.adso.minimarket.exception.NotFoundException;
import org.adso.minimarket.mappers.UserMapper;
import org.adso.minimarket.models.user.Role;
import org.adso.minimarket.models.user.User;
import org.adso.minimarket.repository.jpa.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public User createUser(RegisterRequest body) {
        User usr = new User(body.firstName(), body.lastName(), body.email(), body.password());
        usr.setRole(Role.USER);
        return userRepository.save(usr);
    }

    @Override
    public BasicUser getUserByEmail(String email) {
        User usr = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return userMapper.toResponseDto(usr);
    }

    @Override
    public BasicUser getBasicUserById(Long id) {
        User usr = this.userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return userMapper.toResponseDto(usr);
    }

    @Override
    public DetailedUser updateUserProfile(UserUpdateRequest dto, Long id) {
        User usr = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (dto.getFirstName() != null) {
            usr.setName(dto.getFirstName());
        }

        if (dto.getLastName() != null) {
            usr.setLastName(dto.getLastName());
        }

        if (dto.getAddress() != null) {
            usr.setAddress(dto.getAddress());
        }

        if (dto.getPhoneNumber() != null) {
            usr.setPhoneNumber(dto.getPhoneNumber());
        }

        User updatedUser = userRepository.save(usr);

        return userMapper.toDetailedUserDto(updatedUser);
    }

    @Override
    public User getUserById(Long id) {
        return this.userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public User getUserInternalByEmail(String email) {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.removeUserById(id);
    }
}
