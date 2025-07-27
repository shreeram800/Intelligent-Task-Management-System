package org.example.userservice.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.example.userservice.entity.StompPrincipal;
import org.example.userservice.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JwtHandshakeInterceptor.class);
    private final JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {

        String token = null;

        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpServletRequest = servletRequest.getServletRequest();
            token = httpServletRequest.getParameter("token"); // Expected via ws://localhost:8081/ws?token=...
        }

        if (token == null || !jwtUtil.validateToken(token)) {
            log.warn("WebSocket handshake rejected. Invalid or missing JWT token.");
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }

        String username = jwtUtil.extractUsername(token);
        if (username == null) {
            log.warn("WebSocket handshake rejected. Username not found in JWT.");
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }

        // Put a custom Principal
        attributes.put("user", username);
        log.info("WebSocket handshake authorized for user: {}", username);
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
        if (exception != null) {
            log.error("WebSocket handshake error: {}", exception.getMessage(), exception);
        }
    }
}
