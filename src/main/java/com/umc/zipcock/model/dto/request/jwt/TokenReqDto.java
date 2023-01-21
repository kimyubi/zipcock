package com.umc.zipcock.model.dto.request.jwt;

import lombok.*;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenReqDto {

    String accessToken;
    String refreshToken;

}
