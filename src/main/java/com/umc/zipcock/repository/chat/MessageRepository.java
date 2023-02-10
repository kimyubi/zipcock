package com.umc.zipcock.repository.chat;

import com.umc.zipcock.model.entity.chat.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
