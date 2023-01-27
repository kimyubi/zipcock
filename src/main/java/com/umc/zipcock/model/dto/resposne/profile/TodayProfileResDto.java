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
    private String city;
    private LocalDateTime createdDate;

    public static TodayProfileResDto createProfile(User user) {
        String cityFromResidence = Arrays.stream(user.getResidence().split(",")).collect(Collectors.toList()).get(0);

        return TodayProfileResDto.builder()
                .thumbnail(user.getThumbnail())
                .nickname(user.getNickname())
                .age(user.getAge())
                .height(user.getHeight())
                .city(cityFromResidence)
                .createdDate(user.getCreatedDate())
                .build();

    }
}
