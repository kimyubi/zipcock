package com.umc.zipcock.model.dto.request.jwt;

import lombok.*;

import javax.validation.constraints.NotNull;

@Builder
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenReqDto {

    @NotNull
    String accessToken;

    @NotNull
    String refreshToken;

}
