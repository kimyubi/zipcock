package com.umc.zipcock.model.dto.resposne.chat;

import com.umc.zipcock.model.entity.chat.Message;
import com.umc.zipcock.model.entity.user.User;

import java.time.LocalDateTime;

public class ChatRoomResDto {

    private String thumbnail;
    private String nickname;
    private LocalDateTime sendDate;
    private String messagePreview;

    public void createChatRoom(User opponent, Message lastMessage) {
        this.thumbnail = opponent.getThumbnail();
        this.nickname = opponent.getNickname();
        this.sendDate = lastMessage.getCreatedDate();
        this.messagePreview = lastMessage.getMessage().substring(0,40);
    }
}
