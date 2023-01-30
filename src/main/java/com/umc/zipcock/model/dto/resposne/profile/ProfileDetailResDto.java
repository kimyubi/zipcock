package com.umc.zipcock.model.dto.resposne.profile;

import com.umc.zipcock.model.entity.user.User;
import com.umc.zipcock.model.entity.user.UserImage;
import io.swagger.annotations.ApiParam;
import lombok.*;

import java.util.LinkedList;
import java.util.List;

@Builder
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDetailResDto {

    private List<String> userImageUrlList = new LinkedList<>();
    private String nickname;
    private Integer age;
    @ApiParam(value = "성별 -> 남성: 0(false), 여성: 1(true)입니다.")
    private Boolean gender;
    private Integer height;
    private String occupation;
    private String interest;
    private String hobby;
    private String personality;
    private String viewPoint;


    public void createProfile(User member) {
        for(UserImage image : member.getUserImageList()){
            this.userImageUrlList.add(image.getImageUrl());
        }

        this.nickname = member.getNickname();
        this.age = member.getAge();
        this.gender = member.getGender();
        this.height = member.getHeight();
        this.occupation = member.getOccupation();
        this.interest = member.getInterest();
        this.hobby = member.getHobby();
        this.personality = member.getPersonality();
        this.viewPoint = member.getViewpoint();
    }
}
