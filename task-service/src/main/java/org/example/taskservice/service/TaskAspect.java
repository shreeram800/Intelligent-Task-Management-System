package org.example.taskservice.service;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.example.taskservice.config.NotificationClient;
import org.example.taskservice.config.UserServiceClient;
import org.example.taskservice.dtos.NotificationEmailRequest;
import org.example.taskservice.dtos.NotificationRequest;
import org.example.taskservice.dtos.TaskResponseDto;
import org.example.taskservice.dtos.UserDto;
import org.example.taskservice.security.AuthTokenProvider;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class TaskAspect {

    private final NotificationClient notificationClient;
    private final UserServiceClient userClient;
    private final TaskService taskService;
    private final AuthTokenProvider tokenProvider;

    @AfterReturning(
            pointcut = "execution(* org.example.taskservice.service.TaskService.assignTask(..))",
            returning = "result"
    )
    public void afterTaskAssignment(JoinPoint joinPoint, Object result) {
        // --- Get the Authorization Header from the current request ---
        String token = tokenProvider.getAuthToken();
        if (token == null) {
            throw new IllegalStateException("Could not find Authorization token in request context.");
        }

        Object[] args = joinPoint.getArgs();
        Long taskId = (Long) args[0];
        Long assignerId = (Long) args[1];
        Long assigneeId = (Long) args[2];

        TaskResponseDto taskResponseDto= taskService.getTaskById(taskId);

        // Fetch user details, passing the token for authentication
        UserDto assigner = userClient.getUserById(assignerId, token);
        UserDto assignee = userClient.getUserById(assigneeId, token);

        // ✅ Send Notification
        NotificationRequest notificationRequest = new NotificationRequest(
                assignerId,
                assigneeId,
                "You have been assigned a new task title: " + taskResponseDto.getTitle() +" by " + assigner.getFirstName()
        );
        notificationClient.sendNotification(notificationRequest);

        // ✅ Send Email
        NotificationEmailRequest emailRequest = new NotificationEmailRequest(
                assignee.getEmail(),
                "New Task Assigned",
                "Hi " + assignee.getFirstName() + ",\n\n" +
                        "You have been assigned a new task title"+ taskResponseDto.getTitle()+" by " + assigner.getFirstName() + ".\n" +
                        "Please check the task dashboard for details.\n\nThanks."
        );
        notificationClient.triggerKafka(emailRequest);
    }
    @AfterReturning(
            pointcut = "execution(* org.example.taskservice.service.TaskService.completeTask(..))",
            returning = "result"
    )
    public void afterTaskCompletion(JoinPoint joinPoint, Object result) {
        // --- Get the Authorization Header from the current request ---
        String token = tokenProvider.getAuthToken();
        if (token == null) {
            // This is a critical failure, as we cannot authenticate with other services.
            throw new IllegalStateException("Could not find Authorization token in request context.");
        }

        Object[] args = joinPoint.getArgs();
        Long taskId = (Long) args[0];
        Long assigneeId = (Long) args[1];

        TaskResponseDto taskResponseDto= taskService.getTaskById(taskId);


        // Fetch user details, passing the token for authentication
        UserDto assignee = userClient.getUserById(assigneeId, token);
        UserDto assigner = userClient.getUserById(taskResponseDto.getCreatedBy(), token);



        // ✅ Send Notification
        NotificationRequest notificationRequest = new NotificationRequest(
                taskResponseDto.getCreatedBy(),
                assigneeId,
                "task assigned by you to " + assigner.getFirstName()+ "is completed"
        );
        notificationClient.sendNotification(notificationRequest);

        // ✅ Send Email
        NotificationEmailRequest emailRequest = new NotificationEmailRequest(
                assigner.getEmail(),
                "Task completed",
                "Hi " + assigner.getFirstName() + ",\n\n" +
                        taskResponseDto.getTitle()+" task assigned by you to " + assigner.getFirstName() + ".\n" +
                        "is completed Please check the details on the dashboard.\n\nThanks."
        );
        notificationClient.triggerKafka(emailRequest);
    }
}