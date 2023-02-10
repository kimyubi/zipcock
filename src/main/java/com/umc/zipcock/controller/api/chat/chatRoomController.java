package com.umc.zipcock.controller.api.chat;

import com.umc.zipcock.model.dto.DefaultRes;
import com.umc.zipcock.model.dto.request.chat.ChatRoomReqDto;
import com.umc.zipcock.model.entity.user.User;
import com.umc.zipcock.service.chat.ChatRoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Api(tags = "채팅방")
@RequestMapping("/api/user")
@RequiredArgsConstructor
@RestController
public class chatRoomController {

    private final ChatRoomService chatRoomService;


    @ApiOperation(value = "채팅방을 생성하거나, 이미 생성된 채팅방이 있는 경우 방의 id를 반환하는 API", notes = "응답 데이터 값으로 채팅방 id를 반환합니다.")
    @PostMapping("/chat/room")
    public ResponseEntity<DefaultRes> JoinChatRoom(@RequestBody ChatRoomReqDto dto) {
            return new ResponseEntity<>(chatRoomService.joinChatRoom(dto), HttpStatus.OK);
    }

    @ApiOperation(value = "현재 로그인하고 있는 회원의 채팅방 목록을 반환하는 API")
    @GetMapping("/chat/room")
    public ResponseEntity<DefaultRes> getChatRoomList(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(chatRoomService.getRoomList(user), HttpStatus.OK);
    }
}
