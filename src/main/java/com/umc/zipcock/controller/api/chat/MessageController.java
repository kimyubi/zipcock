package com.umc.zipcock.controller.api.chat;

import com.umc.zipcock.model.dto.DefaultRes;
import com.umc.zipcock.model.dto.request.chat.MessageReqDto;
import com.umc.zipcock.service.chat.MessageService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "채팅")
@RequiredArgsConstructor
@RestController
@CrossOrigin
public class MessageController {

    private final SimpMessagingTemplate messagingTemplate;
    private final MessageService messageService;

    // 클라이언트에서 "/app/chat/send"로 메시지를 발행하므로 메시지를 받아 처리하기 위해 @MessageMapping을 이용해 메시지를 받아준다.
    @MessageMapping("/chat/send")
    public ResponseEntity<DefaultRes> chat(MessageReqDto message) {

        // 클라이언트로부터 전달받은 메세지를 데이터베이스에 저장한다.
        DefaultRes res = messageService.sendMessage(message);

        // "/topic/chat/수신자ID"를 구독하고 있는 회원에게 메시지를 보낸다.
        messagingTemplate.convertAndSend("/topic/chat/" + message.getReceiverId(), message);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }






}
