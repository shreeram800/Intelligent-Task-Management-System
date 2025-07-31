package org.example.notificationservice.service;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.example.notificationservice.model.Notification;
import org.example.notificationservice.repository.NotificationRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
public class NotificationReadAspect {

    private final NotificationRepository notificationRepository;

    @AfterReturning(
        pointcut = "execution(* org.example.notificationservice.service.NotificationServiceImpl.getNotificationsForUser(..))",
        returning = "result"
    )
    public void markNotificationsAsRead(JoinPoint joinPoint, Object result) {
        if (result instanceof List<?> notifications) {
            List<Notification> unread = notifications.stream()
                .filter(n -> n instanceof Notification && !((Notification) n).isRead())
                .map(n -> {
                    ((Notification) n).setRead(true);
                    return (Notification) n;
                })
                .toList();

            if (!unread.isEmpty()) {
                notificationRepository.saveAll(unread);
            }
        }
    }
}
