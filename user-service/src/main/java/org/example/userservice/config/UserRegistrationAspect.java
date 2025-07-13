package org.example.userservice.config;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.example.userservice.dtos.NotificationEmailRequest;
import org.example.userservice.dtos.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Aspect
@Component
@RequiredArgsConstructor
public class UserRegistrationAspect {

    private final RestTemplate restTemplate;

    @AfterReturning(
            pointcut = "execution(* org.example.userservice.controllers.UserController.registerUser(..))",
            returning = "response"
    )
    public void afterUserRegistration(Object response) {
        if (response instanceof UserResponseDto userResponse) {
            NotificationEmailRequest notification = new NotificationEmailRequest();
            notification.setUserEmail(userResponse.getEmail());
            notification.setSubject("Welcome!");
            notification.setMessage("Hi " + userResponse.getFirstName() + ", thanks for registering.");
            ResponseEntity<String> message = restTemplate.postForEntity("http://localhost:8080api/email/trigger", notification, String.class);
            System.out.println("sent email: "+ message.getBody());
        }
        System.out.println("email not sent");

    }
}
