package org.example.userservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.userservice.dtos.ChatResponseDTO;
import org.example.userservice.entity.Message;
import org.example.userservice.entity.User;
import org.example.userservice.repository.UserRepository;
import org.example.userservice.services.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class WebSocketChatController {

    private final ChatService chatService;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatResponseDTO chatMessage, Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new AccessDeniedException("Missing or invalid authentication context");
        }

        String username = principal.getName();

        // Fetch sender based on authenticated username
        User sender = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Unauthorized: User not found"));

        // Ensure senderId in message matches the authenticated user's ID
        if (!sender.getId().equals(chatMessage.getSenderId())) {
            throw new AccessDeniedException("Sender ID does not match authenticated user");
        }

        // Persist the chat message
        Message savedMessage = chatService.saveMessage(chatMessage);

        // Build destination topic for receiver
        String destination = "/topic/team/" + chatMessage.getTeamId() + "/user/" + chatMessage.getReceiverId();

        // Send message to the destination
        messagingTemplate.convertAndSend(destination, chatMessage);
    }


}
