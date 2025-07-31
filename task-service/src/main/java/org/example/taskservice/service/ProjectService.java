package org.example.taskservice.service;


import org.example.taskservice.dtos.ProjectRequestDto;
import org.example.taskservice.dtos.ProjectResponseDto;

import java.util.List;

public interface ProjectService {
    ProjectResponseDto createProject(ProjectRequestDto dto);
    ProjectResponseDto updateProject(Long id, ProjectRequestDto dto);
    List<ProjectResponseDto> getAllProjects();
    void deleteProject(Long id);
    List<ProjectResponseDto> getProjectsByManagerId(Long managerId);
    List<ProjectResponseDto> getProjectsByUserId(Long userId);
    ProjectResponseDto getProjectById(Long id);
}
