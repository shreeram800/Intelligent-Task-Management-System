package org.example.taskservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.taskservice.config.UserServiceClient;
import org.example.taskservice.dao.TaskRequestDto;
import org.example.taskservice.dao.TaskResponseDto;
import org.example.taskservice.dao.TaskUpdateRequestDto;
import org.example.taskservice.dao.UserDto;
import org.example.taskservice.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    private final UserServiceClient userServiceClient;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TaskResponseDto> createTask(@RequestBody TaskRequestDto request) {
        TaskResponseDto response = taskService.createTask(request);
        System.out.println(response);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMOPLOYEE')")
    public ResponseEntity<TaskResponseDto> updateTask(
            @PathVariable("id") Long taskId,
            @RequestBody TaskUpdateRequestDto request
    ) {
        TaskResponseDto response = taskService.updateTask(taskId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable("id") Long taskId) {
        TaskResponseDto response = taskService.getTaskById(taskId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        List<TaskResponseDto> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/my")
    public ResponseEntity<List<TaskResponseDto>> getMyTasks(
            @RequestHeader("Authorization") String token, // Extract token from request
            Authentication authentication
    ) {
        String username = authentication.getName();
        UserDto user = userServiceClient.getUserByUserName(username, token); // Pass token
        Long userId = user.getId();
        List<TaskResponseDto> tasks = taskService.getTasksByUserId(userId);
        return ResponseEntity.ok(tasks);
    }
    @GetMapping("/manager/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    private ResponseEntity<List<TaskResponseDto>> getTaskByManager(@PathVariable Long id){
        return ResponseEntity.ok(taskService.getTaskByManagerId(id));
    }
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<List<TaskResponseDto>> getTasksByAssignedToUserId(@PathVariable("userId") Long userId) {
        List<TaskResponseDto> tasks = taskService.getTasksByAssignedToUserId(userId);
        return ResponseEntity.ok(tasks);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> softDeleteTask(@PathVariable("id") Long taskId) {
        taskService.softDeleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{taskId}/assign")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<String> assignTask(
            @PathVariable Long taskId,
            @RequestParam Long assignerId, // ID of the user assigning
            @RequestParam Long assigneeId
    ) {
        taskService.assignTask(taskId, assignerId, assigneeId);
        return ResponseEntity.ok("Task assigned successfully.");
    }

    @PostMapping("/{taskId}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'EMPLOYEE')")
    public ResponseEntity<String> completeTask(
            @PathVariable Long taskId,
            @RequestParam Long userId
    ) {
        taskService.completeTask(taskId, userId);
        return ResponseEntity.ok("Task marked as completed.");
    }
}
