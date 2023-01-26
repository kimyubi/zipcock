package com.umc.zipcock.service.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umc.zipcock.error.UserNotFoundException;
import com.umc.zipcock.model.dto.DefaultRes;
import com.umc.zipcock.model.dto.request.jwt.TokenReqDto;
import com.umc.zipcock.model.dto.request.user.EmailCheckReqDto;
import com.umc.zipcock.model.dto.request.user.JoinReqDto;
import com.umc.zipcock.model.dto.request.user.LoginReqDto;
import com.umc.zipcock.model.dto.resposne.auth.OauthToken;
import com.umc.zipcock.model.dto.resposne.jwt.TokenResDto;
import com.umc.zipcock.model.entity.jwt.RefreshToken;
import com.umc.zipcock.model.entity.user.User;
import com.umc.zipcock.model.enumClass.user.Role;
import com.umc.zipcock.model.util.JwtTokenProvider;
import com.umc.zipcock.model.util.KakaoProfile;
import com.umc.zipcock.repository.jwt.RefreshTokenRepository;
import com.umc.zipcock.repository.user.JoinTermRepository;
import com.umc.zipcock.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;
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

    // 로그인
    @Transactional
    public TokenResDto login(LoginReqDto dto) {

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


    // 회원가입
    @Transactional(rollbackFor = Exception.class)
    public DefaultRes join(JoinReqDto dto) {

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

    // 토큰 재발급
    @Transactional
    public DefaultRes reissue(User User, TokenReqDto dto) {

        if (!jwtTokenProvider.validateToken(dto.getRefreshToken()))
            return DefaultRes.response(HttpStatus.OK.value(),"Refresh Token이 만료되었습니다.");


        String accessToken = dto.getAccessToken();
        Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);

        if(!authentication.getName().equals(User.getEmail()))
            return DefaultRes.response(HttpStatus.OK.value(),"잘못된 Access Token입니다.");

        List<RefreshToken> refreshTokenList = refreshTokenRepository.findByKey(User.getId());

        RefreshToken refreshToken = refreshTokenList.get(refreshTokenList.size()-1); // 가장 최근에 갱신된 RefreshToken을 꺼낸다.

        // 리프레시 토큰 불일치 에러
        if (!refreshToken.getToken().equals(dto.getRefreshToken()))
            return DefaultRes.response(HttpStatus.OK.value(),"잘못된 Refresh Token입니다.");


        // AccessToken & RefreshToken 토큰 재발급 후, RefreshToken 저장
        TokenResDto newToken = jwtTokenProvider.createToken(User.getEmail(), User.getId(), User.getRoleList());
        RefreshToken updateRefreshToken = refreshToken.updateToken(newToken.getRefreshToken());
        refreshTokenRepository.save(updateRefreshToken);

        return DefaultRes.response(HttpStatus.OK.value(),"토큰 재발급에 성공하였습니다.", newToken);
    }

    // 이메일 중복 확인
    @Transactional(readOnly = true)
    public DefaultRes checkEmail(EmailCheckReqDto dto) {
        Optional<User> existUser = userRepository.findByEmail(dto.getEmail());

        if (existUser.isPresent())
            return DefaultRes.response(HttpStatus.OK.value(),"이미 사용중인 이메일입니다.");
        
        return DefaultRes.response(HttpStatus.OK.value(),"사용 가능한 이메일입니다.");

    }

    public OauthToken getAccessToken(String code) {

        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", "60ab03c60f246c42b6397bfd30eb7900");
        params.add("redirect_uri", "http://localhost:8080/oauth");
        params.add("code", code);
        // params.add("client_secret", "{시크릿 키}");

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);

        ResponseEntity<String> accessTokenResponse = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        OauthToken oauthToken = null;
        try {
            oauthToken = objectMapper.readValue(accessTokenResponse.getBody(), OauthToken.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return oauthToken;
    }

    public DefaultRes saveKakaoUser(String token) {

        KakaoProfile profile = findProfile(token);

        User user = userRepository.findByKakaoEmail(profile.getKakao_account().getEmail());

        if(user == null) {
            user = new User(profile.getId(), profile.getKakao_account().email, profile.getKakao_account().getProfile().getNickname());
            user.getRoleList().add(Role.MEMBER.getTitle());
            userRepository.save(user);
        }

        // Access Token과 Refresh Token 새로 발급
        TokenResDto tokenResDto = jwtTokenProvider.createToken(user.getEmail(), user.getId(), user.getRoleList());

        // Refresh Token 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(user.getId())
                .token(tokenResDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return DefaultRes.response(HttpStatus.OK.value(), "로그인에 성공하였습니다.", tokenResDto);

    }

    private KakaoProfile findProfile(String token) {
        RestTemplate rt = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
                new HttpEntity<>(headers);

        ResponseEntity<String> kakaoProfileResponse = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(kakaoProfileResponse.getBody(), KakaoProfile.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoProfile;
    }
}
