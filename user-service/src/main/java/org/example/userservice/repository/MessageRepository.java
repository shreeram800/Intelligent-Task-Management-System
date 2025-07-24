package org.example.userservice.repository;

import org.example.userservice.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByTeamIdAndSenderIdOrReceiverId(Long teamId, Long senderId, Long receiverId);

    List<Message> findByReceiverIdAndReadFalse(Long receiverId);
}
