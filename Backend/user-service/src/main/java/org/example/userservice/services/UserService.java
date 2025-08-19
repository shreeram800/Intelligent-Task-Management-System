package org.example.userservice.services;

import org.example.userservice.dtos.*;

import java.util.List;

public interface UserService {

    UserResponseDto registerUser(UserRegisterRequestDto request);

    AuthResponseDto loginUser(UserLoginRequestDto request);

    UserResponseDto getUserById(Long id);

    UserResponseDto updateUser(Long id, UserUpdateRequestDto request);

    void softDeleteUser(Long id);

    UserResponseDto getUserByUserName(String userName);

    List<UserResponseDto> getAllUsers();

    List<UserResponseDto> getUsersByMangerId(Long id);

    List<UserResponseDto> getUsersByIds(List<Long> userIds);
}
