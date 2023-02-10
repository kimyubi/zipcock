package com.umc.zipcock.repository.chat;

import com.umc.zipcock.model.entity.chat.ChatRoom;
import com.umc.zipcock.model.entity.user.User;

import java.util.List;

public interface ChatRoomRepositoryExtension {
    List<ChatRoom> findAllIncludeUser(User currentUser);
}
