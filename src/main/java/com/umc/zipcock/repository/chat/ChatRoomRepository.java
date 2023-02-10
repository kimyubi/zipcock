package com.umc.zipcock.repository.chat;

import com.umc.zipcock.model.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryExtension {
    Optional<ChatRoom> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
}
