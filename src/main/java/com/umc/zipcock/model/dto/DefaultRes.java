package com.umc.zipcock.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class DefaultRes<T> {
    // API 상태 코드
    private Integer statusCode;

    // API 부가 설명
    private String Message;

    // API 응답 데이터
    private T data;

    // 페이징 처리
    private Pagination pagination;

    // 상태 코드 + 부가 설명 반환
    public static <T> DefaultRes<T> response(final Integer statusCode, final String Message){
        return (DefaultRes<T>)DefaultRes.builder()
                .statusCode(statusCode)
                .Message(Message)
                .build();
    }

    // 상태 코드 + 부가 설명 + 응답 데이터 반환
    public static <T> DefaultRes<T> response(final Integer statusCode, final String Message, final T data){
        return (DefaultRes<T>)DefaultRes.builder()
                .statusCode(statusCode)
                .Message(Message)
                .data(data)
                .build();
    }

    // 상태 코드 + 부가 설명 + 응답 데이터 + 페이징 처리 반환
    public static <T> DefaultRes<T> response(final Integer statusCode, final String Message, final T data, final Pagination pagination){
        return (DefaultRes<T>)DefaultRes.builder()
                .statusCode(statusCode)
                .Message(Message)
                .data(data)
                .pagination(pagination)
                .build();
    }

}
