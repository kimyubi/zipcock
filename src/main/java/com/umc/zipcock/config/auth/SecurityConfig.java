package com.umc.zipcock.config.auth;

import com.umc.zipcock.model.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Spring Security 설정을 위한 클래스
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()

                // JWT 인증 방식을 사용하기 때문에 session이 필요하지 않음.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // h2-console 화면을 사용하기 위해 해당 옵션 disable
                .headers().frameOptions().disable()
                .and()

                // URI별 권한 관리
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .and()

                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .logout()
                .logoutSuccessUrl("/");
    }

    private static final String[] AUTH_WHITELIST ={
            "/","/css/**","/images/**","/js/**","/h2-console/**","/v3/api-docs/**","/swagger-ui/**"
    };
}
