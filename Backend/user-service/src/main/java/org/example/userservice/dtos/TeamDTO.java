package org.example.userservice.dtos;

import lombok.Data;

import java.util.List;

@Data
public class TeamDTO {
    private Long id;
    private String name;
    private String description;
    private UserResponseDto manager;
    private List<UserResponseDto> users;
}