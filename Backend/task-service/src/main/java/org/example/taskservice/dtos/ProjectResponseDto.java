package org.example.taskservice.dtos;

import lombok.*;
import org.example.taskservice.entity.ProjectStatus;
import org.springframework.scheduling.config.Task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponseDto {

    private Long id;
    private String name;
    private String description;
    private ProjectStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Task> tasks;
    private List<Long> userIds;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long managerId;

}
