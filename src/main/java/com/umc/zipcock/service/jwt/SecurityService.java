package com.umc.zipcock.service.jwt;

import com.umc.zipcock.error.UserNotFoundException;
import com.umc.zipcock.model.dto.DefaultRes;
import com.umc.zipcock.model.dto.request.user.MemberReqDto;
import com.umc.zipcock.model.dto.resposne.jwt.TokenResDto;
import com.umc.zipcock.model.entity.jwt.RefreshToken;
import com.umc.zipcock.model.entity.user.User;
import com.umc.zipcock.model.util.JwtTokenProvider;
import com.umc.zipcock.repository.jwt.RefreshTokenRepository;
import com.umc.zipcock.repository.user.JoinTermRepository;
import com.umc.zipcock.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.umc.zipcock.error.UserNotFoundException.WRONG_PASSWORD;


@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    private final JoinTermRepository joinTermRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public TokenResDto login(MemberReqDto dto) {

        // email 확인
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new UserNotFoundException());

        // password 확인
        if(!passwordEncoder.matches(dto.getPassword(), user.getPassword())){
            throw new UserNotFoundException(WRONG_PASSWORD);
        }

        
        // Access Token과 Refresh Token 새로 발급
        TokenResDto tokenResDto = jwtTokenProvider.createToken(user.getEmail(), user.getId(), user.getRoleList());

        // Refresh Token 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(user.getId())
                .token(tokenResDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);
        return tokenResDto;
    }


    @Transactional(rollbackFor = Exception.class)
    public DefaultRes join(MemberReqDto dto) {

        Optional<User> existUser = userRepository.findByEmail(dto.getEmail());

        if(existUser.isPresent())
            return DefaultRes.response(HttpStatus.OK.value(),"이미 가입된 이메일입니다.");

        User user = dto.toEntity();
        User member = userRepository.save(user);

        member.encodePassword(passwordEncoder);
        member.addUserRole();

        if(member.getId() == null)
            return DefaultRes.response(HttpStatus.OK.value(),"서버의 오류로 회원가입에 실패하였습니다.");

        TokenResDto tokenResDto = jwtTokenProvider.createToken(user.getEmail(), user.getId(), user.getRoleList());

        RefreshToken refreshToken = RefreshToken.builder()
                .key(user.getId())
                .token(tokenResDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return DefaultRes.response(HttpStatus.OK.value(),"회원 가입에 성공하였습니다.", tokenResDto);
    }
}
