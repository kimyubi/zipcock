package com.umc.zipcock.controller.api.user;

import com.umc.zipcock.model.dto.DefaultRes;
import com.umc.zipcock.model.dto.request.user.ProfileReqDto;
import com.umc.zipcock.model.entity.user.User;
import com.umc.zipcock.service.jwt.SecurityService;
import com.umc.zipcock.service.user.ProfileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
