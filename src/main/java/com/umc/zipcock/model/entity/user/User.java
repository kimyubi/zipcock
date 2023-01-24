package com.umc.zipcock.model.entity.user;

import com.umc.zipcock.model.enumClass.user.Role;
import com.umc.zipcock.model.util.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Entity

// local db로 사용 중인 h2 database에서 'user'가 예약어로 사용되기 때문
// 배포 단계에서 @Table 삭제 예정
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column
    private String name;

    @Column
    private String nickname;

    @Column
    private Boolean gender;

    @Column
    private Float age;

    // 거주지
    @Column
    private String residence;

    // 직업
    @Column
    private String occupation;

    // 키
    @Column
    private Integer height;

    // 관심사
    @Column
    private String interest;

    // 취미
    @Column
    private String hobby;

    // 성격
    @Column
    private String personality;

    // 대표 사진
    @Column
    private String thumbnail;

    @Column(nullable = false)
    private String password;

    // 권한
    // @Enumerated(EnumType.STRING)
    // private Role role;

    // 권한의 종류가 매우 적고, user 정보를 가져올 때마다 권한에 대한 정보는 항상 필요하므로 EAGER 모드로 가져오도록 설정
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roleList = new ArrayList<>();

    // user의 프로필을 눌러, 게시한 모든 프로필 사진을 조회하는 경우 외에는 대표 사진으로 표시되므로 LAZY 모드로 가져오도록 설정
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<UserImage> userImageList = new LinkedList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roleList
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() { /** email을 아이디로 사용 **/
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Builder
    public User(String email, String password, String name){
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public void encodePassword(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(password);
    }

    public void addUserRole() {
        this.roleList.add(Role.MEMBER.getTitle());
    }
}
