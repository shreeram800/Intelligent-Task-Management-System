package org.example.notificationservice.model;

import lombok.*;

@Data
@NoArgsConstructor
public class NotificationEmailRequest {
    private String userEmail;
    private String subject;
    private String message;
}
