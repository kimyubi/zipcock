package com.umc.zipcock.service.chat;

import com.umc.zipcock.model.dto.DefaultRes;
import com.umc.zipcock.model.dto.request.chat.ChatRoomReqDto;
import com.umc.zipcock.model.dto.resposne.chat.ChatRoomResDto;
import com.umc.zipcock.model.entity.chat.ChatRoom;
import com.umc.zipcock.model.entity.chat.Message;
import com.umc.zipcock.model.entity.user.User;
import com.umc.zipcock.repository.chat.ChatRoomRepository;
import com.umc.zipcock.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    // 채팅방을 생성하거나, 이미 생성된 채팅방이 있는 경우 방의 id를 반환하는 메소드
    public DefaultRes joinChatRoom(ChatRoomReqDto dto) {
        Optional<User> optionalSender = userRepository.findById(dto.getSenderId());
        Optional<User> optionalReceiver = userRepository.findById(dto.getReceiverId());

        if (optionalSender.isEmpty())
            return DefaultRes.response(HttpStatus.OK.value(), "발신자 ID에 해당하는 회원이 없습니다.");

        if (optionalReceiver.isEmpty())
            return DefaultRes.response(HttpStatus.OK.value(), "수신자 ID에 해당하는 회원이 없습니다.");

        User sender = optionalSender.get();
        User receiver = optionalReceiver.get();

        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.findBySenderIdAndReceiverId(dto.getSenderId(), dto.getSenderId());

        if(optionalChatRoom.isPresent())
            return DefaultRes.response(HttpStatus.OK.value(), "이미 생성된 채팅방이 존재합니다.", optionalChatRoom.get().getId());

        ChatRoom newChatRoom = dto.toEntity(sender,receiver);
        ChatRoom savedChatRoom = chatRoomRepository.save(newChatRoom);

        if (savedChatRoom == null)
            return DefaultRes.response(HttpStatus.OK.value(), "서버의 오류로 채팅방 생성에 실패했습니다.");

        return DefaultRes.response(HttpStatus.OK.value(), "새 채팅방 생성에 성공하였습니다.", savedChatRoom.getId());
    }

    // 현재 로그인하고 있는 회원의 채팅방 목록을 반환하는 메소드
    public DefaultRes getRoomList(User currentUser) {
        // 현재 로그인하고 있는 회원이 참여하고 있는 모든 채팅방 조회
        List<ChatRoom> chatRoomList = chatRoomRepository.findAllIncludeUser(currentUser);
        
        if(chatRoomList.isEmpty())
            return DefaultRes.response(HttpStatus.OK.value(), "채팅방 목록이 존재하지 않습니다.");

        List<ChatRoomResDto> chatRoomDtoList = new LinkedList<>();

        for(ChatRoom chatRoom: chatRoomList){
            int lastIndex = chatRoom.getMessageList().lastIndexOf(Message.class);
            Message lastMessage = chatRoom.getMessageList().get(lastIndex);

            User opponent = null;

            if(currentUser.equals(chatRoom.getSender()))
                opponent = chatRoom.getReceiver();

            else
                opponent = chatRoom.getSender();

            ChatRoomResDto chatRoomDto = new ChatRoomResDto();
            chatRoomDto.createChatRoom(opponent, lastMessage);

            chatRoomDtoList.add(chatRoomDto);
        }


        return DefaultRes.response(HttpStatus.OK.value(), "현재 로그인하고 있는 회원의 채팅방 목록을 반환하는 API 응답에 성공하였습니다.", chatRoomDtoList);
    }
}
