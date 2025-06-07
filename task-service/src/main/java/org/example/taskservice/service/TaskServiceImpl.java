package org.example.taskservice.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.example.taskservice.dao.TaskRequestDto;
import org.example.taskservice.dao.TaskResponseDto;
import org.example.taskservice.dao.TaskUpdateRequestDto;
import org.example.taskservice.entity.*;
import org.example.taskservice.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    @Override
    public TaskResponseDto createTask(TaskRequestDto request) {
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .assignedToUserId(request.getAssignedToUserId())
                .projectId(request.getProjectId())
                .status(TaskStatus.valueOf(request.getStatus().toUpperCase()))
                .priority(request.getPriority() != null ? TaskPriority.valueOf(request.getPriority().toUpperCase()) : null)
                .dueDate(request.getDueDate())
                .createdBy(request.getCreatedBy())
                .deleted(false)
                .build();

        Task saved = taskRepository.save(task);
        return mapToResponseDto(saved);
    }

    @Override
    public TaskResponseDto updateTask(Long taskId, TaskUpdateRequestDto request) {
        Task task = taskRepository.findById(taskId)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        if (request.getTitle() != null) task.setTitle(request.getTitle());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getAssignedToUserId() != null) task.setAssignedToUserId(request.getAssignedToUserId());
        if (request.getProjectId() != null) task.setProjectId(request.getProjectId());
        if (request.getStatus() != null) task.setStatus(TaskStatus.valueOf(request.getStatus().toUpperCase()));
        if (request.getPriority() != null) task.setPriority(TaskPriority.valueOf(request.getPriority().toUpperCase()));
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());
        if (request.getUpdatedBy() != null) task.setUpdatedBy(request.getUpdatedBy());

        return mapToResponseDto(taskRepository.save(task));
    }

    @Override
    public TaskResponseDto getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .filter(t -> !t.isDeleted())
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        return mapToResponseDto(task);
    }

    @Override
    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .filter(task -> !task.isDeleted())
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> getTasksByAssignedToUserId(Long userId) {
        return taskRepository.findByAssignedToUserIdAndDeletedFalse(userId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TaskResponseDto> getTasksByCreatedBy(Long userId) {
        return taskRepository.findByCreatedByAndDeletedFalse(userId).stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void softDeleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        task.setDeleted(true);
        taskRepository.save(task);
    }

    @Override
    public List<TaskResponseDto> getTasksByUserId(Long userId) {
        List<Task> tasks = taskRepository.findByAssignedToUserId(userId);
        return tasks.stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    private TaskResponseDto mapToResponseDto(Task task) {
        return TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .assignedToUserId(task.getAssignedToUserId())
                .projectId(task.getProjectId())
                .status(task.getStatus().name())
                .priority(task.getPriority() != null ? task.getPriority().name() : null)
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .deleted(task.isDeleted())
                .createdBy(task.getCreatedBy())
                .updatedBy(task.getUpdatedBy())
                .build();
    }
}
