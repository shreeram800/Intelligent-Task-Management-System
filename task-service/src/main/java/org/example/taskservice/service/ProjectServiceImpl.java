package org.example.taskservice.service;

import lombok.RequiredArgsConstructor;
import org.example.taskservice.config.ProjectMapper;
import org.example.taskservice.config.UserServiceClient;
import org.example.taskservice.dtos.ProjectRequestDto;
import org.example.taskservice.dtos.ProjectResponseDto;
import org.example.taskservice.dtos.UserDto;
import org.example.taskservice.entity.Project;
import org.example.taskservice.repository.ProjectRepository;
import org.example.taskservice.security.AuthTokenProvider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final UserServiceClient serviceClient;
    private final ProjectRepository projectRepository;
    private final AuthTokenProvider tokenProvider;
    private final ProjectMapper projectMapper;

    @Override
    public ProjectResponseDto createProject(ProjectRequestDto dto) {
        UserDto userDto = serviceClient.getUserById(dto.getManagerId(),tokenProvider.getAuthToken());
        if(userDto==null || !userDto.getRole().equals("MANAGER")){
            throw new IllegalArgumentException("Invalid user");
        }
        for(Long id: dto.getUserIds()){
            if(serviceClient.getUserById(id, tokenProvider.getAuthToken())==null){
                throw new IllegalArgumentException("User doesn't exist by id " + id);
            }
        }
        Project project = projectMapper.toEntity(dto);
        Project saved = projectRepository.save(project);
        return projectMapper.toDto(saved);
    }

    @Override
    public ProjectResponseDto updateProject(Long id, ProjectRequestDto dto) {
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with ID: " + id));

        UserDto userDto = serviceClient.getUserById(dto.getManagerId(),tokenProvider.getAuthToken());
        if(userDto==null || userDto.getRole().equals("MANAGER")){
            throw new IllegalArgumentException("Invalid user");
        }
        existing.setName(dto.getName());
        existing.setDescription(dto.getDescription());
        existing.setStatus(dto.getStatus());
        existing.setStartDate(dto.getStartDate());
        existing.setEndDate(dto.getEndDate());
        existing.setManager(dto.getManagerId());

        Project updated = projectRepository.save(existing);
        return projectMapper.toDto(updated);
    }

    @Override
    public ProjectResponseDto getProjectById(Long id) {
        Project project = projectRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found with ID: " + id));
        return projectMapper.toDto(project);
    }

    @Override
    public List<ProjectResponseDto> getAllProjects() {
        return projectRepository.findAll()
                .stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteProject(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new RuntimeException("Project not found with ID: " + id);
        }
        projectRepository.deleteById(id);
    }

    @Override
    public List<ProjectResponseDto> getProjectsByManagerId(Long managerId) {
        List<Project> projects = projectRepository.findByManager(managerId);
        return projects.stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectResponseDto> getProjectsByUserId(Long userId) {
        List<Project> projects = projectRepository.findByUserId(userId);
        return projects.stream()
                .map(projectMapper::toDto)
                .collect(Collectors.toList());
    }
}
