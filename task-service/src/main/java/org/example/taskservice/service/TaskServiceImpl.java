package org.example.taskservice.service;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.example.taskservice.config.UserServiceClient;
import org.example.taskservice.dtos.*;
import org.example.taskservice.entity.*;
import org.example.taskservice.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserServiceClient userClient;
    private final AttachmentService attachmentService;

    @Override
    public TaskResponseDto createTask(TaskRequestDto request, List<MultipartFile> attachments) throws IOException {

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
        Task savedTask = taskRepository.save(task);

        List<Attachment> attachmentList = new ArrayList<>();
        if (attachments != null && !attachments.isEmpty()) {
            for (MultipartFile file : attachments) {
                try {
                    Attachment attachment = Attachment.builder()
                            .fileName(file.getOriginalFilename())
                            .fileType(file.getContentType())
                            .fileSize(file.getSize())
                            .data(file.getBytes())
                            .task(savedTask)
                            .build();
                    attachmentList.add(attachment);
                } catch (IOException e) {
                    throw new RuntimeException("Failed to read file: " + file.getOriginalFilename(), e);
                }
            }
        }
        attachmentService.uploadFile(attachmentList);

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

    public TaskResponseDto getTaskById(Long taskId) {
        System.out.println(taskId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        // Use projections to build AttachmentMetaDto list
        List<AttachmentMetaProjection> projections = attachmentService.getProjection(taskId);
        List<AttachmentMetaDto> attachmentDtos = projections.stream()
                .map(p -> AttachmentMetaDto.builder()
                        .id(p.getId())
                        .fileName(p.getFileName())
                        .fileType(p.getFileType())
                        .fileSize(p.getFileSize())
                        .downloadUrl(ServletUriComponentsBuilder.fromCurrentContextPath()
                                .path("/api/tasks/attachments/download/")
                                .path(String.valueOf(p.getId()))
                                .toUriString())
                        .build())
                .toList();

        return TaskResponseDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(String.valueOf(task.getStatus()))
                .priority(String.valueOf(task.getPriority()))
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .createdBy(task.getCreatedBy())
                .attachments(attachmentDtos)
                .build();
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

    @Override
    public List<TaskResponseDto> getTaskByManagerId(Long managerId) {
        return taskRepository.getAllByCreatedBy(managerId).stream()
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
    @Override
    public void assignTask(Long taskId, Long assignerId, Long assigneeId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        // Validate assigner has permission (e.g., is MANAGER or ADMIN)
        UserDto assigner = userClient.getUserById(assignerId);
        if (!assigner.getRole().equalsIgnoreCase("MANAGER") &&
                !assigner.getRole().equalsIgnoreCase("ADMIN")) {
            throw new RuntimeException("User not authorized to assign tasks");
        }

        // Validate assignee exists
        UserDto assignee = userClient.getUserById(assigneeId);
        if (assignee == null || assignee.isDeleted()) {
            throw new RuntimeException("Assignee is invalid or deleted");
        }

        task.setAssignedToUserId(assigneeId);
        task.setStatus(TaskStatus.TODO);
        task.setUpdatedBy(assignerId);
        taskRepository.save(task);
    }

    @Override
    public void completeTask(Long taskId, Long userId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        System.out.println(task.getAssignedToUserId() + " " + userId );

        if (!task.getAssignedToUserId().equals(userId)) {
            throw new RuntimeException("Only the assigned user can complete the task");
        }
        task.setStatus(TaskStatus.DONE);
        task.setUpdatedBy(userId);
        taskRepository.save(task);
    }
}
