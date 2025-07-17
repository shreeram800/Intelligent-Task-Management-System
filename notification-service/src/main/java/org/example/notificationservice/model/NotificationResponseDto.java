package org.example.notificationservice.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificationResponseDto {

    private Long id;

    private Long senderId;

    private Long receiverId;

    private String senderName;

    private String receiverName;

    private String message;

    private boolean read;

    private LocalDateTime createdAt;
}
