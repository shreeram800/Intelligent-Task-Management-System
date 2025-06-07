package org.example.taskservice.dao;

import lombok.*;

import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TaskRequestDto {

    @Size(max = 100, message = "Title must be at most 100 characters")
    private String title;

    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    private Long assignedToUserId;

    private Long projectId;

    private String status;

    private String priority;

    private LocalDateTime dueDate;

    private Long createdBy;

    private Long updatedBy;
}
