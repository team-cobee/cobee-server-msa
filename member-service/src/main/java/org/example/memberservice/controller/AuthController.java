package org.example.memberservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.common.apiPayload.response.ApiResponse;
import org.example.common.constant.GatewayConstant;
import org.example.memberservice.dto.MemberInfoDto;
import org.example.memberservice.dto.TokenRefreshRequest;
import org.example.memberservice.dto.TokenRefreshResponse;
import org.example.memberservice.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @GetMapping
    public ApiResponse<?> getMemberInfo(@RequestHeader(GatewayConstant.GATEWAY_AUTH_HEADER) Long memberId) {
        MemberInfoDto memberInfo = authService.getMemberInfo(memberId);
        return ApiResponse.success("사용자 정보 조회 성공", "200", memberInfo);
    }
    @PostMapping("/refresh")
    public ApiResponse<TokenRefreshResponse> refreshToken(@RequestBody TokenRefreshRequest request) {
        TokenRefreshResponse tokenRefreshResponse = authService.refreshTokens(request.getRefreshToken());
        return ApiResponse.success("토큰 재발급 성공", "TOKEN_REFRESH_SUCCESS", tokenRefreshResponse);
    }

    @PostMapping("/logout")
    public ApiResponse<MemberInfoDto> logout (
            @RequestHeader(GatewayConstant.GATEWAY_AUTH_HEADER) Long memberId) {
        MemberInfoDto memberInfoDto = authService.logout(memberId);
        return ApiResponse.success("로그아웃 성공", "200", memberInfoDto);
    }
}
