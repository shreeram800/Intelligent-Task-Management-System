package org.example.userservice.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor; // Import this
import org.example.userservice.security.JwtUtil; // Import your JwtUtil
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor // Use Lombok to create the constructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JwtHandshakeInterceptor.class);

    // Inject the centralized JwtUtil service
    private final JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        String token = null;

        if (request instanceof org.springframework.http.server.ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
            token = httpServletRequest.getParameter("token"); // <-- Most reliable for SockJS
        }

        if (token == null || !jwtUtil.validateToken(token)) {
            log.warn("WebSocket handshake forbidden. Token was null or invalid.");
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }

        String username = jwtUtil.extractUsername(token);
        attributes.put("user", username);

        log.info("WebSocket handshake successful for user: {}", username);
        return true;
    }


    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        if (exception != null) {
            // The exception message here can be very helpful for debugging
            log.error("Exception after WebSocket handshake: {}", exception.getMessage());
        }
    }

    // The local isValidJwt and getUserFromToken methods are no longer needed and can be deleted.
}