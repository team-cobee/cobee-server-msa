package org.example.alarmservice.service;

import lombok.RequiredArgsConstructor;
import org.example.alarmservice.domain.PushToken;
import org.example.alarmservice.dto.RegisterTokenRequest;
import org.example.alarmservice.repository.PushTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PushTokenService {

    private final PushTokenRepository repo;

    /** 토큰 등록 (중복 시 업데이트) */
    public void registerOrUpdate(RegisterTokenRequest req) {
        repo.findByToken(req.getToken()).ifPresentOrElse(
                token -> {
                    token.setActive(true);
                    token.setUserId(req.getUserId());
                },
                () -> {
                    PushToken newToken = PushToken.builder()
                            .userId(req.getUserId())
                            .token(req.getToken())
                            .platform(req.getPlatform())
                            .active(true)
                            .build();
                    repo.save(newToken);
                }
        );
    }

    /** 특정 사용자 토큰 전체 조회 */
    @Transactional(readOnly = true)
    public List<String> getTokensByUserId(String userId) {
        return repo.findAllByUserIdAndActiveTrue(userId)
                .stream()
                .map(PushToken::getToken)
                .toList();
    }

    /** 토큰 비활성화 (예: invalid token 시) */
    public void deactivateToken(String token) {
        repo.findByToken(token).ifPresent(t -> t.setActive(false));
    }
}
