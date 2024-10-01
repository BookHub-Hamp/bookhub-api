package com.hampcode.repository;

import com.hampcode.model.entity.ChatMessage;
import com.hampcode.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findBySender(User sender);
}
