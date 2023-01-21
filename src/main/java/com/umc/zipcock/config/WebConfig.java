package com.umc.zipcock.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")                           // CORS 정책을 허용할 URI 패턴 설정
                .allowedOrigins("http://localhost:3000")               // CORS 정책을 허용할 출처 설정 (추후 배포 작업 시 변경)
                .allowedMethods("*")                                   // CORS 정책을 허용할 HTTP 메소드 설정
                .allowCredentials(false)                               // 쿠키 요청 허용 여부
//                .allowedHeaders("*")                                 // JWT 구현 후 추가
//                .exposedHeaders("Authorization")
                .maxAge(3000);                                         // preflight 요청에 대한 응답을 브라우저에서 캐싱하는 시간

     }
}
