package org.example.taskservice.service;


import org.example.taskservice.dtos.ProjectRequestDto;
import org.example.taskservice.dtos.ProjectResponseDto;

import java.util.List;
import java.util.UUID;

public interface ProjectService {
    ProjectResponseDto createProject(ProjectRequestDto dto);
    ProjectResponseDto updateProject(UUID id, ProjectRequestDto dto);
    ProjectResponseDto getProject(UUID id);
    List<ProjectResponseDto> getAllProjects();
    void deleteProject(UUID id);
}
