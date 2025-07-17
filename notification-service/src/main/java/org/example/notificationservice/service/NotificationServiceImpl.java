package org.example.notificationservice.service;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.After;
import org.example.notificationservice.model.Notification;
import org.example.notificationservice.model.NotificationResponseDto;
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
    public List<NotificationResponseDto> getNotificationsForUser(Long userId) {
        List<Notification> notifications = notificationRepository.findByReceiverIdOrderByCreatedAtDesc(userId);
        return notifications.stream().map(NotificationServiceImpl::toDto).toList();
    }
    public static NotificationResponseDto toDto(Notification notification) {
        if (notification == null) return null;

        NotificationResponseDto dto = new NotificationResponseDto();
        dto.setId(notification.getId());
        dto.setSenderId(notification.getSenderId());
        dto.setReceiverId(notification.getReceiverId());
        dto.setSenderName(notification.getSenderName());
        dto.setReceiverName(notification.getReceiverName());
        dto.setMessage(notification.getMessage());
        dto.setRead(notification.isRead());
        dto.setCreatedAt(notification.getCreatedAt());

        return dto;
    }
}
