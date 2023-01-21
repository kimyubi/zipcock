package com.umc.zipcock.service.jwt;

import com.umc.zipcock.error.UserNotFoundException;
import com.umc.zipcock.model.dto.request.user.EmailDto;
import com.umc.zipcock.model.dto.resposne.jwt.TokenResDto;
import com.umc.zipcock.model.entity.jwt.RefreshToken;
import com.umc.zipcock.model.entity.user.User;
import com.umc.zipcock.model.util.JwtTokenProvider;
import com.umc.zipcock.repository.jwt.RefreshTokenRepository;
import com.umc.zipcock.repository.user.JoinTermRepository;
import com.umc.zipcock.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.Valid;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JoinTermRepository joinTermRepository;

    @Transactional
    public TokenResDto login(@Valid EmailDto dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException("가입되지 않은 이메일입니다."));

        TokenResDto tokenResDto = jwtTokenProvider.createToken(user.getEmail(), user.getId(), user.getRoleList());

        // Refresh Token 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(user.getId())
                .token(tokenResDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);
        return tokenResDto;
    }
}
