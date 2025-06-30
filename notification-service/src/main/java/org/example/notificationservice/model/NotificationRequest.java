package org.example.notificationservice.model;

import lombok.*;

@Data
@NoArgsConstructor
public class NotificationRequest {
    private String userEmail;
    private String subject;
    private String message;
}
