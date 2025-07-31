package org.example.notificationservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.notificationservice.model.NotificationEmailRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class KafkaTriggerEmailController {

    private final KafkaTemplate<String, NotificationEmailRequest> kafkaTemplate;

    @PostMapping("/trigger")
    public String triggerKafka(@RequestBody NotificationEmailRequest request) {
        kafkaTemplate.send("task-events", request);
        return "Kafka event triggered!";
    }
}
