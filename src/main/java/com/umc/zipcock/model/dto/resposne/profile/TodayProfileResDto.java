package com.umc.zipcock.model.dto.resposne.profile;

import com.umc.zipcock.model.entity.user.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

@Builder @Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class TodayProfileResDto {
    private String thumbnail;       // 회원 프로필 사진
    private String nickname;
    private Integer age;
    private Integer height;
    private String city;            // 시 (행정 구역 단위)
    private String district;        // 구 (행정 구역 단위)
    private LocalDateTime createdDate;

    public static TodayProfileResDto createProfile(User user) {
        String cityFromResidence = Arrays.stream(user.getResidence().split(",")).collect(Collectors.toList()).get(0);
        String districtFromResidence = Arrays.stream(user.getResidence().split(",")).collect(Collectors.toList()).get(1);


        return TodayProfileResDto.builder()
                .thumbnail(user.getThumbnail())
                .nickname(user.getNickname())
                .age(user.getAge())
                .height(user.getHeight())
                .city(cityFromResidence)
                .district(districtFromResidence)
                .createdDate(user.getCreatedDate())
                .build();

    }
}
