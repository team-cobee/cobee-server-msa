package org.example.memberservice.service;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.memberservice.domain.Member;
import org.example.memberservice.dto.MemberInfoDto;
import org.example.memberservice.dto.MemberResponseDto;
import org.example.memberservice.dto.TokenRefreshResponse;
import org.example.memberservice.jwt.JwtProvider;
import org.example.memberservice.repository.MemberRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final JwtProvider jwtProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final MemberRepository memberRepository;

    /**
     * RefreshToken을 받아서 새로운 AccessToken과 RefreshToken을 발급
     * RTR(Refresh Token Rotation) 전략을 사용하여 매번 새로운 RefreshToken을 발급
     */
    @Transactional
    public TokenRefreshResponse refreshTokens(String refreshToken) {
        // 1. RefreshToken 유효성 검증
        if (!jwtProvider.validateToken(refreshToken)) {
            throw new RuntimeException("유효하지 않은 Refresh Token");
        }

        // 2. RefreshToken에서 memberId추출
        String memberId = jwtProvider.getMemberIdFromToken(refreshToken);
        // 3. Redis에 저장된 RefreshToken과 비교
        String storedRefreshToken = redisTemplate.opsForValue().get(memberId);
        if (storedRefreshToken == null || !storedRefreshToken.equals(refreshToken)) {
            throw new RuntimeException("저장된 Refresh Token과 일치하지 않습니다. 재로그인이 필요합니다.");
        }

        // 4. 새로운 AccessToken과 RefreshToken 생성
        String newAccessToken = jwtProvider.createAccessToken(memberId);
        String newRefreshToken = jwtProvider.createRefreshToken(memberId);  // memberId 전달

        // 5. Redis에 새로운 RefreshToken 저장 (기존 것은 자동으로 덮어씌워짐)
        redisTemplate.opsForValue().set(
                memberId,
                newRefreshToken,
                7,
                TimeUnit.DAYS
        );
        log.info("토큰 재발급 완료 - memberId: {}", memberId);

        // 6. 응답 생성
        return TokenRefreshResponse.of(
                newAccessToken,
                newRefreshToken,
                jwtProvider.refreshTokenValiditySeconds
        );
    }

    public MemberInfoDto getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("해당 멤버를 찾을 수 없습니다."));
        return MemberInfoDto.from(member);
    }

    public MemberInfoDto logout(Long memberId) {
        // Redis에서 RefreshToken 삭제
        redisTemplate.delete(String.valueOf(memberId));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        return MemberInfoDto.from(member);

    }
}
