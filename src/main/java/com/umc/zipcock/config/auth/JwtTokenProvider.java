package com.umc.zipcock.config.auth;

import com.umc.zipcock.model.dto.resposne.jwt.TokenResDto;
import com.umc.zipcock.service.jwt.CustomUserDetailsService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import java.util.List;

// JWT 토큰 생성, 토큰 복호화 및 정보 추출, 토큰의 유효성 검증의 기능을 구현한 클래스
@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider {

    // 토큰의 암호화/복호화를 위한 secret key
    private String secretKey = "umc-zipcock";

    // Refresh Token 유효 기간 14일 (ms 단위)
    private final Long REFRESH_TOKEN_VALID_TIME = 14 * 1440 * 60 * 1000L;

    // Access Token 유효 기간 15분
    private final Long ACCESS_TOKEN_VALID_TIME =  15 * 60 * 1000L;

    private final CustomUserDetailsService userDetailsService;

    // 의존성 주입이 완료된 후에 실행되는 메소드, secretKey를 Base64로 인코딩한다.
    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성
    // JWT는 .을 기준으로 header, payload, signature 으로 이루어져 있다.
    public TokenResDto createToken(String userEmail, Long userId, List<String> roleList){

        // payload 에는 토큰에 담을 정보가 들어가는데 이때, 정보의 단위를 클레임(claim)이라고 부르며, 클레임은 key-value 의 한 쌍으로 이루어져 있다.
        // 토큰 제목 설정
        Claims claims = Jwts.claims().setSubject(userEmail);

        claims.put("id", userId);
        claims.put("role_list", roleList);

        Date now = new Date();

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_VALID_TIME))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return TokenResDto.builder()
                .grantType("bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .accessTokenExpireDate(ACCESS_TOKEN_VALID_TIME)
                .build();
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 사용자 인증 정보 조회
    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    private String getUserEmail(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    // Request의 Header로부터 토큰 값 조회
    public String resolveToken(HttpServletRequest request){
        return request.getHeader("Authorization");
    }

    // 토큰의 만료 일자를 확인하여 토큰의 유효성을 검증
    public boolean validateToken(String jwtToken){

        try{
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e){
            return false;
        }
    }
}
