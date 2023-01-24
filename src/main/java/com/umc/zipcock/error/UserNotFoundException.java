package com.umc.zipcock.error;

public class UserNotFoundException extends RuntimeException {

    public static String USER_NOT_FOUND ="입력하신 이메일로 가입된 사용자가 존재하지 않습니다.";
    public static String WRONG_PASSWORD ="잘못된 비밀번호입니다.";
    public static String USER_NOT_LOGIN ="로그인이 필요한 작업입니다.";

    public UserNotFoundException(String message) {
        super(message);
    }
    public UserNotFoundException() {
        super(USER_NOT_FOUND);
    }
}
