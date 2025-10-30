package org.example.alarmservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.alarmservice.client.NhnPushClient;
import org.example.alarmservice.dto.CreateAlarmRequest;
import org.example.alarmservice.dto.RegisterTokenRequest;
import org.example.alarmservice.service.AlarmNoticeService;
import org.example.common.apiPayload.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AlarmController {
    private final AlarmNoticeService alarmNoticeService;
    private final NhnPushClient nhnPushClient;

    @PostMapping("/internal/alarms")
    public ApiResponse<Void> createAlarm(@RequestBody @Valid CreateAlarmRequest request) {
        try {
            alarmNoticeService.createNotice(request);
            return ApiResponse.success("알림 생성 및 발송 요청 완료", "ALARM_CREATED", null);
        } catch (Exception e) {
            log.error("알림 생성 중 오류 발생: {}", request, e);
            return ApiResponse.failure("INTERNAL_SERVER_ERROR", "알림 생성에 실패했습니다.", null);
        }
    }

    @PostMapping("/tokens")
    public Mono<ResponseEntity<Void>> registerToken(@RequestBody @Valid RegisterTokenRequest request) {
        return nhnPushClient.registerToken(request.getUserId(), request.getToken())
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .onErrorResume(e -> {
                    log.error("토큰 등록 중 오류 발생: {}", request, e);
                    return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).<Void>build());
                });
    }
}
