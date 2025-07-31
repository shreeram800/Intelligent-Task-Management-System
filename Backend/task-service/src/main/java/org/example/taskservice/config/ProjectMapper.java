package org.example.taskservice.config;

import org.example.taskservice.dtos.ProjectRequestDto;
import org.example.taskservice.dtos.ProjectResponseDto;
import org.example.taskservice.dtos.UserDto;
import org.example.taskservice.entity.Project;
import org.example.taskservice.security.AuthTokenProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Objects;

@Component
public class ProjectMapper {

    private  final UserServiceClient userServiceClient;

    private final AuthTokenProvider authTokenProvider;

    public ProjectMapper(UserServiceClient userServiceClient, AuthTokenProvider authTokenProvider) {
        this.userServiceClient = userServiceClient;
        this.authTokenProvider = authTokenProvider;
    }

    public Project toEntity(ProjectRequestDto dto) {
        UserDto userDto= userServiceClient.getUserById(dto.getManagerId(),authTokenProvider.getAuthToken());
        if(userDto == null ) {
            throw new IllegalArgumentException("Invalid user");
        }
        if(!Objects.equals(userDto.getRole(), "MANAGER")) {
            throw new IllegalArgumentException("Invalid role");
        }
        return Project.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .manager(dto.getManagerId())
                .tasks(null)
                .userIds(dto.getUserIds() != null ? dto.getUserIds() : new ArrayList<>())
                .build();
    }

    public ProjectResponseDto toDto(Project entity) {
        return ProjectResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .status(entity.getStatus())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .managerId(entity.getManager())
                .userIds(entity.getUserIds())
                .build();
    }
}
