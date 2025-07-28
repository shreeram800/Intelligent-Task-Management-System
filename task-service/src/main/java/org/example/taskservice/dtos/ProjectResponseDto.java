package org.example.taskservice.dtos;

import lombok.*;
import org.example.taskservice.entity.ProjectStatus;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectResponseDto {

    private UUID id;
    private String name;
    private String description;
    private ProjectStatus status;

    private LocalDate startDate;
    private LocalDate endDate;

    private List<UUID> taskIds;
    private List<UUID> userIds;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long managerId;
}
