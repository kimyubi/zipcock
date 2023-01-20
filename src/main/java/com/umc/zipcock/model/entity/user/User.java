package com.umc.zipcock.model.entity.user;

import com.umc.zipcock.model.util.BaseEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Entity

// local db로 사용 중인 h2 database에서 'user'가 예약어로 사용되기 때문
// 배포 단계에서 @Table 삭제 예정
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
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

    // 권한
    // @Enumerated(EnumType.STRING)
    // private Role role;

    // 권한의 종류가 매우 적고, user 정보를 가져올 때마다 권한에 대한 정보는 항상 필요하므로 EAGER 모드로 가져오도록 설정
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roleList = new ArrayList<>();

    // user의 프로필을 눌러, 게시한 모든 프로필 사진을 조회하는 경우 외에는 대표 사진으로 표시되므로 LAZY 모드로 가져오도록 설정
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<UserImage> userImageList = new LinkedList<>();

}
