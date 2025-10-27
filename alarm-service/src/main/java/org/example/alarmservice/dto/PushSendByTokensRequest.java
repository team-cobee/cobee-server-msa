package org.example.alarmservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PushSendByTokensRequest {
    @NotEmpty
    private List<String> tokens;   // 바로 토큰 타겟팅
    @NotBlank
    private String title;
    @NotBlank private String body;
    private Map<String, String> data;        // 화면 이동 등 커스텀 데이터
}
