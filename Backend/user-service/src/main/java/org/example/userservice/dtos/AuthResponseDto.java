package org.example.userservice.dtos;

public record AuthResponseDto(String token, UserResponseDto user) {}
