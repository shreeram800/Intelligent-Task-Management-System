package org.example.taskservice.service;

import org.example.taskservice.dtos.TaskRequestDto;
import org.example.taskservice.dtos.TaskResponseDto;
import org.example.taskservice.dtos.TaskUpdateRequestDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface TaskService {
    TaskResponseDto createTask(TaskRequestDto request, List<MultipartFile> attachments) throws IOException;
    TaskResponseDto getTaskById(Long id);

    List<TaskResponseDto> getAllTasks();

    List<TaskResponseDto> getTasksByAssignedToUserId(Long userId);
    TaskResponseDto updateTask(Long id, TaskUpdateRequestDto request);

    List<TaskResponseDto> getTasksByCreatedBy(Long userId);

    void softDeleteTask(Long id);

    List<TaskResponseDto> getTasksByUserId(Long userId);

    List<TaskResponseDto> getTaskByManagerId(Long managerId);

    void assignTask(Long taskId, Long assignerId, Long assigneeId);

    void completeTask(Long taskId, Long userId);
}
