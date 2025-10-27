package org.example.alarmservice.client;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class NhnPushClient {

    private final WebClient webClient = WebClient.builder().build();

    @Value("${nhn.push.base-url}")
    private String baseUrl;

    @Value("${nhn.push.app-key}")
    private String appKey;

    @Value("${nhn.push.secret-key}")
    private String secretKey;

    /**
     * NHN에 토큰을 사전 등록(바인딩)하는 API가 있으면 여기에 구현.
     * 엔드포인트/스키마는 NHN 문서대로
     */
    @Retry(name = "nhnPush") @CircuitBreaker(name = "nhnPush")
    public Mono<Void> registerToken(String userId, String token) {
        Map<String, Object> body = Map.of(
                "userId", userId,
                "token", token,
                "pushType", "FCM"     // iOS라면 APNS
        );

        String path = String.format("%s/push/v2/appkeys/%s/tokens", baseUrl, appKey);

        return webClient.post()
                .uri(path)
                .header("X-Secret-Key", secretKey) // 헤더 키 이름은 문서 확인
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(r -> log.info("NHN token registered userId={}, status={}", userId, r.getStatusCode()))
                .then();
    }

    /**
     * 토큰 목록으로 직접 발송 (사전 등록 없이)
     * NHN의 실제 요청 스키마/엔드포인트와 필드명은 문서대로
     */
    @Retry(name = "nhnPush") @CircuitBreaker(name = "nhnPush")
    public Mono<Void> sendByTokens(List<String> tokens, String title, String body, Map<String, String> data) {
        Map<String, Object> payload = Map.of(
                "target", Map.of(
                        "type", "TOKEN",           // TOKEN or USER_ID
                        "to", tokens
                ),
                "content", Map.of(
                        "default", Map.of("title", title, "body", body),
                        "android", Map.of(
                                "title", title,
                                "body", body,
                                "data", data != null ? data : Map.of()
                        )
                ),
                "options", Map.of(
                        "scheduleType", "IMMEDIATE"
                )
        );

        String path = String.format("%s/push/v2/appkeys/%s/messages", baseUrl, appKey); // 체크하기

        return webClient.post()
                .uri(path)
                .header("X-Secret-Key", secretKey) // 체크하기
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .toBodilessEntity()
                .doOnSuccess(r -> log.info("NHN push sent (tokens.size={}), status={}", tokens.size(), r.getStatusCode()))
                .then();
    }

    /**
     * (선택) userId 대상으로 발송 (사전 바인딩이 된 경우)
     */
    @Retry(name = "nhnPush") @CircuitBreaker(name = "nhnPush")
    public Mono<Void> sendByUserIds(List<String> userIds, String title, String body, Map<String, String> data) {
        Map<String, Object> payload = Map.of(
                "target", Map.of(
                        "type", "USER_ID",
                        "to", userIds
                ),
                "content", Map.of(
                        "default", Map.of("title", title, "body", body),
                        "android", Map.of(
                                "title", title,
                                "body", body,
                                "data", data != null ? data : Map.of()
                        )
                ),
                "options", Map.of("scheduleType", "IMMEDIATE")
        );

        String path = String.format("%s/push/v2/appkeys/%s/messages", baseUrl, appKey); // 체크하기

        return webClient.post()
                .uri(path)
                .header("X-Secret-Key", secretKey) // 체크하기
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .toBodilessEntity()
                .then();
    }
}

