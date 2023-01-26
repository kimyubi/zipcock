package com.umc.zipcock.model.dto.request.user;


import io.swagger.annotations.ApiParam;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class ProfileReqDto {

    @Size(min = 2, max = 5, message = "프로필 사진은 2장 이상 5장 이하로 등록해야 합니다.")
    @ApiParam(value = "이미지 URL을 보내주세요.")
    private List<String> userImageUrlList;

    private String nickname;

    @ApiParam(value = "성별 -> 남성: 0, 여성: 1로 보내주세요.")
    private Boolean gender;

    @Min(1) @Max(99)
    private Integer age;

    @Min(100) @Max(230)
    @ApiParam(value = "키, 단위(cm)")
    private Integer height;

    @ApiParam(value = "거주지는 콤마(,)로 구분하여 보내주세요. \n Ex) 안산시,상록구 / 서울특별시,서대문구 ")
    private String residence;

    @ApiParam(value = "직업")
    private String occupation;

    @ApiParam(value = "성격은 콤마(,)로 구분하여 보내주세요. \n Ex) 창의적인,지적인,차분한")
    private String personality;

    @ApiParam(value = "관심사")
    private String interest;

    @ApiParam(value = "취미")
    private String hobby;

    @ApiParam(value = "가치관")
    private String viewPoint;

}
