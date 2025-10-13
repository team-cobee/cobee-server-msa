package org.example.memberservice.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // csrf(웹 해킹 공격 방식) 보호 비활성화 <- 네이티브 앱 사용하므로
                .formLogin(form -> form.disable()) // 폼 로그인 비활성화
                .httpBasic(httpBasic -> httpBasic.disable()) // http 기본 인증 비활성화
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService)) // 로그인 성공 시 사용자 정보를 받아와 회원가입 또는 회원정보 업데이트
                        .successHandler(/* 여기에 로그인 성공 후 JWT를 만들어줄 핸들러가 들어갈 거예요 */)
                );
        return http.build();
    }
}
