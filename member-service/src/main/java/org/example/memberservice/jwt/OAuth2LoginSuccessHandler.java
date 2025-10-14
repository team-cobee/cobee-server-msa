package org.example.memberservice.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.apiPayload.error.code.BaseErrorCode;
import org.example.common.apiPayload.error.exception.CustomException;
import org.example.memberservice.domain.Member;
import org.example.memberservice.repository.MemberRepository;
import org.example.memberservice.security.OAuthAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 로그인 성공 - 핸들러 처리");
        try {
            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

            // OAuthAttributes로 변환
            OAuthAttributes attributes = OAuthAttributes.of("response", oAuth2User.getAttributes());

            // DB에서 회원 정보 조회
            Member member = memberRepository.findByEmail(attributes.getEmail())
                    .orElseThrow(() -> new CustomException(BaseErrorCode._INTERNAL_SERVER_ERROR)); // 일단 현재 코드를 사용하고 추후 common 라이브러리 코드 수정

            String accessToken = jwtProvider.createAccessToken(String.valueOf(member.getId()));
            log.info("발급된 Access Token: {}", accessToken);

            // 백엔드 테스트를 위해, API Gateway의 임의 경로로 토큰을 실어 리다이렉트합니다.
            String targetUrl = UriComponentsBuilder.fromUriString("http://localhost:8080/login/success")
                    .queryParam("accessToken", accessToken)
                    .build()
                    .encode(StandardCharsets.UTF_8)
                    .toUriString();

            log.info("테스트를 위해 다음 주소로 리다이렉트합니다: {}", targetUrl);

            clearAuthenticationAttributes(request);
            // 리다이렉트 실행
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
        } catch (Exception e) {
            log.error("로그인 성공 후 처리 중 에러 발생", e);
            throw e;
        }
    }
}
