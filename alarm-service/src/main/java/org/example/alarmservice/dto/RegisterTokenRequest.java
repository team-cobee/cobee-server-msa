package org.example.alarmservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class RegisterTokenRequest {

    @NotBlank
    private String userId;

    @NotBlank
    private String token;
}