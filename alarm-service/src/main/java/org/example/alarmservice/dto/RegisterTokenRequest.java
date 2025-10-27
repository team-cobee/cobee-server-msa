package org.example.alarmservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterTokenRequest {
    @NotBlank
    private String userId;     // 앱의 사용자 식별자
    @NotBlank private String token;      // FCM registration token
    @Builder.Default private String platform = "ANDROID";
}