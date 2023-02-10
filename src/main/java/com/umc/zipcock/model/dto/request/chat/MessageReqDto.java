package com.umc.zipcock.model.dto.request.chat;

import com.umc.zipcock.model.entity.chat.ChatRoom;
import com.umc.zipcock.model.entity.chat.Message;
import com.umc.zipcock.model.entity.user.User;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
public class MessageReqDto {

    @NotBlank(message = "메시지는 필수 입력 값입니다.")
    private String message;

    @NotNull(message = "발신자 id는 필수 입력 값입니다.")
    private Long senderId;

    @NotNull(message = "수신자 id는 필수 입력 값입니다.")
    private Long receiverId;

    @NotNull(message = "채팅방 id는 필수 입력 값입니다.")
    private Long chatRoomId;

    public Message toEntity(User sender, User receiver, ChatRoom chatRoom){
        return Message.builder()
                .sender(sender)
                .receiver(receiver)
                .chatRoom(chatRoom)
                .message(message)
                .build();
    }

}
