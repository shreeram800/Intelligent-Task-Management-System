package org.example.taskservice.config;

import feign.RequestInterceptor;
import jakarta.validation.constraints.NotNull;
import org.example.taskservice.dtos.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@FeignClient(name = "user-service", url = "http://localhost:8081")
public interface UserServiceClient {

    @GetMapping("/api/users/{userId}")
    UserDto getUserById(@NotNull(message = "User ID is required") @PathVariable Long userId, @RequestHeader("Authorization") String token);

    @GetMapping("/api/users/username/{username}")
    UserDto getUserByUserName(
            @PathVariable String username,
            @RequestHeader("Authorization") String token // Forward the token
    );

    @Bean
    default RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            // Get the current HTTP request
            ServletRequestAttributes attributes = (ServletRequestAttributes)
                    RequestContextHolder.getRequestAttributes();

            if (attributes != null) {
                // Extract the JWT token from the Authorization header
                String token = attributes.getRequest().getHeader("Authorization");

                if (token != null) {
                    // Forward the same token to the User Service
                    requestTemplate.header("Authorization", token);
                }
            }
        };
    }
}

