package org.example.notificationservice.service;

import org.example.notificationservice.model.Notification;
import org.example.notificationservice.model.NotificationRequest;

import java.util.List;

public interface
NotificationService {
    void sendNotification(NotificationRequest request);
    List<Notification> getNotificationsForUser(Long userId);
}
