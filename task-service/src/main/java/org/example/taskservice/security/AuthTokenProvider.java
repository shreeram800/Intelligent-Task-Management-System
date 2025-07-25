// Create a new file, e.g., in a 'util' or 'security' package
package org.example.taskservice.security;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class AuthTokenProvider {

    /**
     * Retrieves the Authorization header value from the current request context.
     * @return The full Authorization token (e.g., "Bearer ...").
     * @throws IllegalStateException if the token cannot be found.
     */
    public String getAuthToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes != null) {
            String token = attributes.getRequest().getHeader("Authorization");
            if (token != null && !token.isBlank()) {
                return token;
            }
        }
        // This is a critical failure, as we cannot authenticate with other services.
        throw new IllegalStateException("Could not find Authorization token in request context.");
    }
}