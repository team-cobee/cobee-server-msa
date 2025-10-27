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
public class PushSendByUserIdsRequest {
    @NotEmpty
    private List<String> userIds;  // NHN에 사전 바인딩된 사용자ID 대상 (선택전략)
    @NotBlank private String title;
    @NotBlank
    private String body;
    private Map<String, String> data;
}
