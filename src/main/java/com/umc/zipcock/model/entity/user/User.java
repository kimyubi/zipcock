package com.umc.zipcock.model.entity.user;

import com.umc.zipcock.model.util.BaseEntity;
import lombok.Getter;

import javax.persistence.*;

@Entity

// local db로 사용 중인 h2 database에서 'user'가 예약어로 사용되기 때문
// 배포 단계에서 @Table 삭제 예정
@Table(name = "users")
@Getter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String name;

    private String nickname;

    private boolean gender;

    private int age;

    // 거주지
    private String residence;

    // 직업
    private String occupation;

    // 키
    private float height;

    // 관심사
    private String interest;

    // 취미
    private String hobby;

    // 성격
    private String personality;

    // 대표 사진
    private String thumbnail;













}
