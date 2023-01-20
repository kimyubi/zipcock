package com.umc.zipcock.model.enumClass;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    MEMBER("ROLE_MEMBER", "회원"),
    ADMIN("ROLE_ADMIN", "관리자");

    private final String key;
    private final String title;
}
