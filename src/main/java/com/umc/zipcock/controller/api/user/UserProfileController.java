package com.umc.zipcock.controller.api.user;

import com.umc.zipcock.model.dto.DefaultRes;
import com.umc.zipcock.model.dto.request.user.ProfileReqDto;
import com.umc.zipcock.model.entity.user.User;
import com.umc.zipcock.model.enumClass.user.Role;
import com.umc.zipcock.service.jwt.SecurityService;
import com.umc.zipcock.service.user.ProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@Api(tags = "프로필")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserProfileController {

    private final ProfileService profileService;

    @ApiOperation(value = "프로필 작성 API")
    @PostMapping("/profile")
    public ResponseEntity<DefaultRes> createProfile(@AuthenticationPrincipal User user, @Valid @RequestBody ProfileReqDto dto) {
        return new ResponseEntity<>(profileService.createProfile(user, dto), HttpStatus.OK);
    }

    @ApiOperation(value = "홈(오늘의 소개) API", notes = "최근 회원가입을 한 순으로 최대 10명의 상대의 프로필을 보여줍니다.")
    @GetMapping("/today-profile")
    public ResponseEntity<DefaultRes> retrieveTodayProfile(@AuthenticationPrincipal User user) {
        return new ResponseEntity<>(profileService.retrieveTodayProfile(user), HttpStatus.OK);
    }


}
