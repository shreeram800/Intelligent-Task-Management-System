package org.example.taskservice.dao;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TaskResponseDto {

    private Long id;

    private String title;

    private String description;

    private Long assignedToUserId;

    private Long projectId;

    private String status;

    private String priority;

    private LocalDateTime dueDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean deleted;

    private Long createdBy;

    private Long updatedBy;

    private List<AttachmentMetaDto> attachments;
}
