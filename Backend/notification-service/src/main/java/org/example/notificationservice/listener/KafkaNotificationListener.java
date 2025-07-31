package org.example.notificationservice.listener;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.model.NotificationEmailRequest;
import org.example.notificationservice.service.NotificationEmailService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaNotificationListener {
    private final NotificationEmailService notificationService;

    @KafkaListener(topics = "task-events", groupId = "notification-group", containerFactory = "kafkaListenerContainerFactory")
    public void handleTaskEvent(NotificationEmailRequest request) {
        log.info("📩 Received task event for notification: {}", request);
        notificationService.sendNotification(request);
    }

}
