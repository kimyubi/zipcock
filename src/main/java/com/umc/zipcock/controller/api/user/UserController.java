package com.umc.zipcock.controller.api.user;

import com.umc.zipcock.model.dto.DefaultRes;
import com.umc.zipcock.model.dto.request.jwt.TokenReqDto;
import com.umc.zipcock.model.dto.request.user.EmailCheckReqDto;
import com.umc.zipcock.model.dto.request.user.MemberReqDto;
import com.umc.zipcock.model.dto.resposne.jwt.TokenResDto;
import com.umc.zipcock.model.entity.user.User;
import com.umc.zipcock.service.jwt.SecurityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Email;

@Api(tags = "로그인 / 회원 가입")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final SecurityService securityService;

    @ApiOperation(value = "로그인 API" , notes = "로그인에 성공하면 토큰을 헤더에 넣어서 반환합니다. Authorization 헤더에 AccessToken을 넣어주세요")
    @PostMapping("/login")
    public TokenResDto login(@Valid @RequestBody MemberReqDto dto) {
        return securityService.login(dto);
    }

    @ApiOperation(value = "회원가입 API" , notes = "로그인에 성공하면 토큰을 헤더에 넣어서 반환합니다. Authorization 헤더에 AccessToken을 넣어주세요")
    @PostMapping("/join")
    public DefaultRes join(@Valid @RequestBody MemberReqDto dto){
        return securityService.join(dto);
    }

    @ApiOperation(value = "토큰 만료 시 토큰 재발급을 위한 API")
    @PostMapping("/reissue")
    public DefaultRes reissue(@AuthenticationPrincipal User user, @Valid @RequestBody TokenReqDto dto) {
        return securityService.reissue(user, dto);
    }


    @ApiOperation(value = "이메일 중복 확인을 위한 API")
    @PostMapping("/check-email")
    public DefaultRes checkEmail(@RequestBody @Valid EmailCheckReqDto dto) {
        return securityService.checkEmail(dto);
    }
}
