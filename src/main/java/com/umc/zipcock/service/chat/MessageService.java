package com.umc.zipcock.service.chat;

import com.umc.zipcock.model.dto.DefaultRes;
import com.umc.zipcock.model.dto.request.chat.MessageReqDto;
import com.umc.zipcock.model.entity.chat.ChatRoom;
import com.umc.zipcock.model.entity.chat.Message;
import com.umc.zipcock.model.entity.user.User;
import com.umc.zipcock.repository.chat.ChatRoomRepository;
import com.umc.zipcock.repository.chat.MessageRepository;
import com.umc.zipcock.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    private final ChatRoomRepository chatRoomRepository;
    @Transactional
    public DefaultRes sendMessage(MessageReqDto message) {
        Optional<User> optionalSender = userRepository.findById(message.getSenderId());
        Optional<User> optionalReceiver = userRepository.findById(message.getReceiverId());
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findById(message.getChatRoomId());

        if (optionalSender.isEmpty())
            return DefaultRes.response(HttpStatus.OK.value(), "발신자 ID에 해당하는 회원이 없습니다.");

        if (optionalReceiver.isEmpty())
            return DefaultRes.response(HttpStatus.OK.value(), "수신자 ID에 해당하는 회원이 없습니다.");

        if (optionalChatRoom.isEmpty())
            return DefaultRes.response(HttpStatus.OK.value(), "채팅방 ID에 해당하는 채팅방이 없습니다.");

        User sender = optionalSender.get();
        User receiver = optionalReceiver.get();
        ChatRoom chatRoom = optionalChatRoom.get();

        Message newMessage  = message.toEntity(sender,receiver,chatRoom);
        
        // 메시지를 데이터베이스에 저장
        Message savedMessage = messageRepository.save(newMessage);

        if (savedMessage == null){
            return DefaultRes.response(HttpStatus.OK.value(), "서버의 오류로 메시지 저장에 실패하였습니다.");
        }

        chatRoom.getMessageList().add(savedMessage);

        // 추후 데이터 반환은 삭제하고 응답 메시지만 반환하도록 수정
       return DefaultRes.response(HttpStatus.OK.value(), "메시지 저장에 성공하였습니다.", savedMessage);
    }
}
