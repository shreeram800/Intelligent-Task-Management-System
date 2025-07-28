package org.example.userservice.dtos;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatResponseDTO {
    private Long senderId;
    private Long receiverId;
    private Long teamId;
    private String content;
    private LocalDateTime timestamp;
}
