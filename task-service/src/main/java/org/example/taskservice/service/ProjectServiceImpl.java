package org.example.taskservice.service;

import lombok.RequiredArgsConstructor;
import org.example.taskservice.dtos.ProjectRequestDto;
import org.example.taskservice.dtos.ProjectResponseDto;
import org.example.taskservice.entity.Project;
import org.example.taskservice.repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public ProjectResponseDto createProject(ProjectRequestDto dto) {
        Project project = mapToEntity(dto);
        Project saved = projectRepository.save(project);
        return mapToResponse(saved);
    }

    @Override
    public ProjectResponseDto updateProject(UUID id, ProjectRequestDto dto) {
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + id));

        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setStatus(dto.getStatus());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());
        existing.setTaskIds(dto.getTaskIds());
        existing.setUserIds(dto.getUserIds());

        Project updated = projectRepository.save(existing);
        return mapToResponse(updated);
    }

    @Override
    public ProjectResponseDto getProject(UUID id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + id));
        return mapToResponse(project);
    }

    @Override
    public List<ProjectResponseDto> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProject(UUID id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found with ID: " + id);
        }
        projectRepository.deleteById(id);
    }

    // DTO ↔ Entity Mapping
    private Project mapToEntity(ProjectRequestDto dto) {
        return Project.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .status(dto.getStatus())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .taskIds(dto.getTaskIds())
                .userIds(dto.getUserIds())
                .build();
    }

    private ProjectResponseDto mapToResponse(Project project) {
        return ProjectResponseDto.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .status(project.getStatus())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .taskIds(project.getTaskIds())
                .userIds(project.getUserIds())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
