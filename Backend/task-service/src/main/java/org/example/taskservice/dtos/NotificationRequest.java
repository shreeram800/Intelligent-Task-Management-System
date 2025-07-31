package org.example.taskservice.dtos;

import lombok.Data;

@Data
public class NotificationRequest {
    private Long senderId;
    private Long receiverId;
    private String message;

    public NotificationRequest(Long assignerId, Long assigneeId, String s) {
        this.senderId = assignerId;
        this.receiverId = assigneeId;
        this.message = s;
    }
}
