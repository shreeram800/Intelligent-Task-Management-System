package org.example.userservice.services;

import lombok.RequiredArgsConstructor;
import org.example.userservice.dtos.ChatMessageDTO;
import org.example.userservice.entity.Message;
import org.example.userservice.entity.User;
import org.example.userservice.repository.MessageRepository;
import org.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public Message saveMessage(ChatMessageDTO dto) {
        User sender = userRepository.findById(dto.getSenderId())
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(dto.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        if (sender.getTeam() == null) {
            throw new IllegalStateException("User is not assigned to any team.");
        }

        if (!Objects.equals(sender.getTeam().getId(), receiver.getTeam().getId())) {
            throw new IllegalArgumentException("Users are not in the same team");
        }

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .team(sender.getTeam())
                .content(dto.getContent())
                .read(false)
                .build();

        return messageRepository.save(message);
    }
}
