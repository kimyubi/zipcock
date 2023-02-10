package com.umc.zipcock.model.dto.request.chat;

import com.umc.zipcock.model.entity.chat.ChatRoom;
import com.umc.zipcock.model.entity.user.User;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class ChatRoomReqDto {

    @NotBlank(message = "발신자 id는 필수 입력 값입니다.")
    private Long senderId;

    @NotBlank(message = "수신자 id는 필수 입력 값입니다.")
    private Long receiverId;

    public ChatRoom toEntity(User sender, User receiver) {
        return ChatRoom.builder()
                .sender(sender)
                .receiver(receiver)
                .build();
    }
}
