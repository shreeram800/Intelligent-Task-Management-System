package org.example.userservice.dtos;

import lombok.Data;

@Data
public class NotificationEmailRequest {
    private String userEmail;
    private String subject;
    private String message;
}
