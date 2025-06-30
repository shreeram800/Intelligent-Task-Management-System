package org.example.notificationservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.notificationservice.model.NotificationRequest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
public class KafkaTriggerController {

    private final KafkaTemplate<String, NotificationRequest> kafkaTemplate;

    @PostMapping("/trigger")
    public String triggerKafka(@RequestBody NotificationRequest request) {
        kafkaTemplate.send("task-events", request);
        return "Kafka event triggered!";
    }
}
