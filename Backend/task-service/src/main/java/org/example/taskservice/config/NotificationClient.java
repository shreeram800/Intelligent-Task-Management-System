package org.example.taskservice.config;

import org.example.taskservice.dtos.NotificationEmailRequest;
import org.example.taskservice.dtos.NotificationRequest;
import org.example.taskservice.dtos.NotificationResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "notification-service", url = "http://localhost:8083")
public interface NotificationClient {

    @PostMapping("/api/email/trigger")
    String triggerKafka(@RequestBody NotificationEmailRequest request);

    @PostMapping("/api/notifications/send")
    String sendNotification(@RequestBody NotificationRequest request);

    @GetMapping("/api/notifications/user/{userId}")
    List<NotificationResponseDto> getUserNotifications(@PathVariable("userId") Long userId);

}
