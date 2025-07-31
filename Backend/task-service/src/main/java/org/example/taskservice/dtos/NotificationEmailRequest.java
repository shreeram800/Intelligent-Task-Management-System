package org.example.taskservice.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationEmailRequest {
    private String userEmail;
    private String subject;
    private String message;

    public NotificationEmailRequest(String email, String subject, String message) {
        this.userEmail = email;
        this.subject = subject;
        this.message = message;
    }
}
