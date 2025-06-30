package org.example.notificationservice.listener;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.model.NotificationRequest;
import org.example.notificationservice.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaNotificationListener {
    private final NotificationService notificationService;

    @KafkaListener(topics = "task-events", groupId = "notification-group", containerFactory = "kafkaListenerContainerFactory")
    public void handleTaskEvent(NotificationRequest request) {
        log.info("📩 Received task event for notification: {}", request);
        notificationService.sendNotification(request);
    }

}
