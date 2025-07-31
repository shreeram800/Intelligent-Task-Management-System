package org.example.taskservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.taskservice.dtos.NotificationEmailRequest;
import org.example.taskservice.dtos.NotificationRequest;
import org.example.taskservice.dtos.UserDto;
import org.example.taskservice.entity.Task;
import org.example.taskservice.repository.TaskRepository;
import org.example.taskservice.security.AuthTokenProvider;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskDeadlineScheduler {

    private final TaskRepository taskRepository;
    private final UserServiceClient userServiceClient;
    private final NotificationClient emailService;
    private final AuthTokenProvider tokenProvider;// your existing email sender

    @Scheduled(cron = "0 35 1 * * *", zone = "Asia/Kolkata")// Every day at 9 AM
    public void checkTasksDueTomorrow() {
        LocalDateTime start = LocalDate.now().plusDays(1).atStartOfDay();

        LocalDateTime end = start.plusDays(1);

        Task task = taskRepository.findById(1L).orElse(null);

            try {
                String emailBody = String.format(
                    "Hi,\n\nYou have a task \"%s\" due on %s.\n\nPlease make sure to complete it in time.\n\nThanks,\nTaskFlow System",
                    task.getTitle(),
                    task.getDueDate().toLocalDate()
                );


                UserDto user = userServiceClient.getUserById(task.getAssignedToUserId(),tokenProvider.getAuthToken());

                // Sample email send logic
                emailService.triggerKafka(new NotificationEmailRequest(user.getEmail(),task.getTitle()+" task deadline Today....!!!!!",emailBody));

                emailService.sendNotification(new NotificationRequest(task.getAssignedToUserId(), 0L, task.getTitle()+" task deadline Today....!!!!!"));

                log.info("Reminder sent for task: {}", task.getId());

            } catch (Exception e) {
                log.error("Failed to send reminder for task: {}", task.getId(), e);
            }
        }
    }

