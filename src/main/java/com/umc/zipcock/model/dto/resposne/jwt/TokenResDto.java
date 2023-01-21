package com.umc.zipcock.model.dto.resposne.jwt;

import lombok.*;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenResDto {

    // grantType은 토큰 요청의 유형으로, JWT를 사용하는 요청의 경우에 grantType의 값은 bearer여야 함.
    // 한번 획득된 accessToken은 만료 시점까지 모든 리소스 서버의 엔드포인트 요청 헤더에 Authorization: Bearer {ACCESS_TOKEN}로 첨부된다.
    private String grantType;
    
    // accessToken 만료 시점
    private Long accessTokenExpireDate;

    private String accessToken;
    private String refreshToken;
}
