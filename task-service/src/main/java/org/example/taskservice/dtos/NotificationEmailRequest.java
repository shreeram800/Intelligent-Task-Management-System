package org.example.taskservice.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationEmailRequest {
    private String userEmail;
    private String subject;
    private String message;

    public NotificationEmailRequest(String email, String newTaskAssigned, String s) {
        this.userEmail = email;
        this.subject = newTaskAssigned;
        this.message = s;
    }
}
