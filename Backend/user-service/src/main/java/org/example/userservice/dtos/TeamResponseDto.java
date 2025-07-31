package org.example.userservice.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TeamResponseDto {
    private long id;
    private String name;
    private String description;
    private UserResponseDto manager;
    private List<UserResponseDto> users;
}
