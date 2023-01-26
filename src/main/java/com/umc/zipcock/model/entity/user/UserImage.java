package com.umc.zipcock.model.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.umc.zipcock.model.entity.user.User;
import com.umc.zipcock.model.util.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserImage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    private String imageUrl;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public UserImage(String imageUrl, User user){
        this.imageUrl = imageUrl;
        this.user = user;
    }

}
