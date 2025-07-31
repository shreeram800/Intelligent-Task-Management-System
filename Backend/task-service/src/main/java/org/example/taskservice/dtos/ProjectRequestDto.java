package org.example.taskservice.dtos;

import lombok.*;
import org.example.taskservice.entity.ProjectStatus;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectRequestDto {

    private String name;
    private String description;
    private ProjectStatus status;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<Long> userIds;
    private Long managerId;
}
