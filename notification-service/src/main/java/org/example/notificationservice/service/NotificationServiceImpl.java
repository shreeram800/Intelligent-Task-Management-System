package org.example.notificationservice.service;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.example.notificationservice.model.Notification;
import org.example.notificationservice.model.NotificationRequest;
import org.example.notificationservice.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public void sendNotification(NotificationRequest request) {
        Notification notification = Notification.builder()
                .senderId(request.getSenderId())
                .receiverId(request.getReceiverId())
                .message(request.getMessage())
                .read(false)
                .build();

        notificationRepository.save(notification);
    }

    @Override
    @After("org.example.no")
    public List<Notification> getNotificationsForUser(Long userId) {
        return notificationRepository.findByReceiverIdOrderByCreatedAtDesc(userId);
    }
}
