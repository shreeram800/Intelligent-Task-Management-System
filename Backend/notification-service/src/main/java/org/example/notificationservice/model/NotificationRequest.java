package org.example.notificationservice.model;

import lombok.Data;

@Data
public class NotificationRequest {
    private Long senderId;
    private Long receiverId;
    private String message;
}
