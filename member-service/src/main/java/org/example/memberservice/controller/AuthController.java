package org.example.memberservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.common.apiPayload.response.ApiResponse;
import org.example.memberservice.dto.TokenRefreshRequest;
import org.example.memberservice.dto.TokenRefreshResponse;
import org.example.memberservice.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/refresh")
    public ApiResponse<TokenRefreshResponse> refreshToken(@RequestBody TokenRefreshRequest request) {
        TokenRefreshResponse tokenRefreshResponse = authService.refreshTokens(request.getRefreshToken());
        return ApiResponse.success("토큰 재발급 성공", "TOKEN_REFRESH_SUCCESS", tokenRefreshResponse);
    }
}
