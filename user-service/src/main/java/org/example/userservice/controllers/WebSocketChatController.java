package org.example.userservice.controllers;

import lombok.RequiredArgsConstructor;
import org.example.userservice.dtos.ChatMessageDTO;
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
    public void sendMessage(@Payload ChatMessageDTO chatMessage, Principal principal) {
        // Validate user identity from Principal (usually username or email)
        User sender = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Unauthorized"));

        if (!sender.getId().equals(chatMessage.getSenderId())) {
            throw new AccessDeniedException("Sender ID mismatch");
        }

        Message savedMessage = chatService.saveMessage(chatMessage);
        String destination = "/topic/team/" + chatMessage.getTeamId() + "/user/" + chatMessage.getReceiverId();
        messagingTemplate.convertAndSend(destination, chatMessage);
    }

}
