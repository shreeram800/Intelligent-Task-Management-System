package org.example.userservice.services;

import org.example.userservice.dtos.*;

public interface UserService {

    UserResponseDto registerUser(UserRegisterRequestDto request);

    AuthResponseDto loginUser(UserLoginRequestDto request);

    UserResponseDto getUserById(Long id);

    UserResponseDto updateUser(Long id, UserUpdateRequestDto request);

    void softDeleteUser(Long id);

    UserResponseDto getUserByUserName(String userName);

}
