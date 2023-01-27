package com.umc.zipcock.service.user;

import com.umc.zipcock.model.dto.DefaultRes;
import com.umc.zipcock.model.dto.request.user.ProfileReqDto;
import com.umc.zipcock.model.dto.resposne.profile.TodayProfileResDto;
import com.umc.zipcock.model.entity.user.User;
import com.umc.zipcock.model.entity.user.UserImage;
import com.umc.zipcock.repository.user.UserImageRepository;
import com.umc.zipcock.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;
    private final UserImageRepository userImageRepository;

    @Transactional
    public DefaultRes createProfile(User user, ProfileReqDto dto) {
        List<UserImage> userImageList = new LinkedList<>();

        // 썸네일 추출
        String thumbnail = dto.getUserImageUrlList().get(0);

        for(String imageUrl: dto.getUserImageUrlList()){
            UserImage image = new UserImage(imageUrl, user);
            userImageRepository.save(image);
            userImageList.add(image);
        }

        User member = user.createProfile(dto, userImageList, thumbnail);
        member.addUserRole();
        User savedMember = userRepository.save(member);

        if(savedMember == null)
            return DefaultRes.response(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버의 에러로 프로필 작성에 실패하였습니다.");

        return DefaultRes.response(HttpStatus.OK.value(), "프로필 작성에 성공하였습니다.");

    }

    public DefaultRes retrieveTodayProfile(User currentUser) {
        // 본인을 제외하고, 최근 회원가입을 한 순으로 최대 10명의 프로필을 가져온다.
        List<User> userList =  userRepository.getTodayProfile(currentUser);

        if (userList.isEmpty())
            return DefaultRes.response(HttpStatus.OK.value(),"회원이 없습니다.");

        List<TodayProfileResDto> todayProfileList = new LinkedList<>();

        for(User user: userList){
            TodayProfileResDto profile = TodayProfileResDto.createProfile(user);
            todayProfileList.add(profile);
        }

        return DefaultRes.response(HttpStatus.OK.value(),"홈(오늘의 소개) API 응답에 성공하였습니다.", todayProfileList);
    }

    public DefaultRes retrieveAroundProfile(User currentUser) {
        // 본인을 제외하고, 최근 회원가입을 한 순으로 최대 10명의 프로필을 가져온다.
        List<User> userList =  userRepository.getAroundProfile(currentUser);

        if (userList.isEmpty())
            return DefaultRes.response(HttpStatus.OK.value(),"회원이 없습니다.");

        List<TodayProfileResDto> todayProfileList = new LinkedList<>();

        for(User user: userList){
            TodayProfileResDto profile = TodayProfileResDto.createProfile(user);
            todayProfileList.add(profile);
        }

        return DefaultRes.response(HttpStatus.OK.value(),"홈(오늘의 소개) API 응답에 성공하였습니다.", todayProfileList);
    }
}
