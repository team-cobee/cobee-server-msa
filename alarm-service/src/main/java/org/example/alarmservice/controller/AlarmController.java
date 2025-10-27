package org.example.alarmservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.alarmservice.client.NhnPushClient;
import org.example.alarmservice.dto.PushSendByTokensRequest;
import org.example.alarmservice.dto.RegisterTokenRequest;
import org.example.alarmservice.service.PushTokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/alarm")
@RequiredArgsConstructor
@Slf4j
public class AlarmController {
    private final PushTokenService pushTokenService;
    private final NhnPushClient nhn;

    @PostMapping("/register")
    public Mono<ResponseEntity<Void>> register(@RequestBody @Valid RegisterTokenRequest req) {
        try {
            pushTokenService.registerOrUpdate(req);
            return Mono.just(ResponseEntity.ok().build());
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    @PostMapping("/send/user/{userId}")
    public Mono<ResponseEntity<Void>> sendToUser(@PathVariable String userId, @RequestBody @Valid PushSendByTokensRequest req) {
        try{
            var tokens = pushTokenService.getTokensByUserId(userId);
            return nhn.sendByTokens(tokens, req.getTitle(), req.getBody(), req.getData())
                    .thenReturn(ResponseEntity.accepted().build());
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }

    }

}
