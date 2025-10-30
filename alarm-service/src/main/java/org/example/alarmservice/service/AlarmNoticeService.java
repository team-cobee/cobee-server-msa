package org.example.alarmservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.alarmservice.client.NhnPushClient;
import org.example.alarmservice.domain.Alarm;
import org.example.alarmservice.domain.AlarmNotice;
import org.example.alarmservice.dto.CreateAlarmRequest;
import org.example.alarmservice.repository.AlarmNoticeRepository;
import org.example.alarmservice.repository.AlarmRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlarmNoticeService {
    private final AlarmRepository alarmRepository;
    private final AlarmNoticeRepository alarmNoticeRepository;
    private final PushTokenService pushTokenService;
    private final NhnPushClient nhnPushClient;

    public void createNotice(CreateAlarmRequest request) {
        Alarm alarm = Alarm.builder()
                .alarmType(request.getAlarmType())
                .sourceType(request.getSourceType())
                .sourceId(request.getSourceId())
                .fromUserId(request.getFromUserId())
                .build();
        alarmRepository.save(alarm);

        AlarmNotice notice = AlarmNotice.builder()
                .alarm(alarm)
                .toUserId(request.getToUserId())
                .title(request.getTitle())
                .body(request.getBody())
                .build();
        alarmNoticeRepository.save(notice);

        List<String> tokens = pushTokenService.getTokensByUserId(String.valueOf(request.getToUserId()));
        if (tokens.isEmpty()) {
            log.debug("No active push tokens for user {}", request.getToUserId());
            return;
        }

        nhnPushClient.sendByTokens(tokens, request.getTitle(), request.getBody(), request.getData())
                .doOnError(error -> log.warn("Failed to send push notification to user {}: {}", request.getToUserId(), error.getMessage()))
                .subscribe();

       // return AlarmInfoResponse;
    }
}