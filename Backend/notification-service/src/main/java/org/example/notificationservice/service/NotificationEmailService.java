package org.example.notificationservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.model.NotificationEmailRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationEmailService {

    private final JavaMailSender mailSender;

    public void sendNotification(NotificationEmailRequest request) {
        log.info("📧 Sending email to {}...", request.getUserEmail());

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(request.getUserEmail());
            message.setSubject(request.getSubject());
            message.setText(request.getMessage());
            message.setFrom("shreeramdubey800@gmail.com");
            mailSender.send(message);

            log.info("✅ Email sent to {}", request.getUserEmail());
        } catch (Exception e) {
            log.error("❌ Failed to send email to {}", request.getUserEmail(), e);
        }
    }
}
