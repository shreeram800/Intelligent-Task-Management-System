package org.example.userservice.services;


import lombok.RequiredArgsConstructor;
import org.example.userservice.Exceptions.ResourceNotFoundException;
import org.example.userservice.dtos.*;
import org.example.userservice.entity.User;
import org.example.userservice.repository.UserRepository;
import org.example.userservice.security.JwtUtil;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UserResponseDto registerUser(UserRegisterRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail()) || userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Email or Username already exists.");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .profilePictureUrl(request.getProfilePictureUrl())
                .role("EMPLOYEE")
                .isAuthenticated(false)
                .isEmailVerified(false)
                .status("ACTIVE")
                .deleted(false)
                .build();

        User saved = userRepository.save(user);
        return mapToResponseDto(saved);
    }

    @Override
    public AuthResponseDto loginUser(UserLoginRequestDto request) {
        User user = userRepository.findByEmailOrUsername(request.getUsernameOrEmail(), request.getUsernameOrEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        user.setAuthenticated(true);
        user.setLastLoginAt(LocalDateTime.now());
        userRepository.save(user);

        String token = jwtUtil.generateToken(user.getUsername(), List.of(user.getRole()));
        return new AuthResponseDto(token, mapToResponseDto(user));

    }

    @Override
    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return mapToResponseDto(user);
    }

    @Override
    public UserResponseDto updateUser(Long id, UserUpdateRequestDto request) {
        User user = userRepository.findById(id)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (request.getFirstName() != null) user.setFirstName(request.getFirstName());
        if (request.getLastName() != null) user.setLastName(request.getLastName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getUsername() != null) user.setUsername(request.getUsername());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
        if (request.getProfilePictureUrl() != null) user.setProfilePictureUrl(request.getProfilePictureUrl());

        return mapToResponseDto(userRepository.save(user));
    }

    @Override
    public void softDeleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setDeleted(true);
        user.setStatus("INACTIVE");
        userRepository.save(user);
    }

    @Override
    public UserResponseDto getUserByUserName(String userName) {
        Optional<User> userOptional = userRepository.getUserByUsername(userName);

        User user = userOptional.orElseThrow(() ->
                new UsernameNotFoundException("User with username '" + userName + "' not found")
        );

        return mapToResponseDto(user);
    }

    private UserResponseDto mapToResponseDto(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .isAuthenticated(user.isAuthenticated())
                .isEmailVerified(user.isEmailVerified())
                .phoneNumber(user.getPhoneNumber())
                .profilePictureUrl(user.getProfilePictureUrl())
                .status(user.getStatus())
                .build();
    }
}
