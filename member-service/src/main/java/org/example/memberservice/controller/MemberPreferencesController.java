package org.example.memberservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.apiPayload.response.ApiResponse;
import org.example.common.constant.GatewayConstant;
import org.example.memberservice.dto.MemberPreferencesRequestDto;
import org.example.memberservice.dto.MemberPreferencesResponseDto;
import org.example.memberservice.service.MemberPreferenceService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/preferences")
@Slf4j
@RequiredArgsConstructor
public class MemberPreferencesController {

    private final MemberPreferenceService memberPreferenceService;

    @PostMapping()
    public ApiResponse<MemberPreferencesResponseDto> createMemberPreferences(
            @RequestHeader(GatewayConstant.GATEWAY_AUTH_HEADER) Long memberId,
            @RequestBody MemberPreferencesRequestDto requestDto
    )
    {
        MemberPreferencesResponseDto responseDto = memberPreferenceService.createMemberPreferences(memberId, requestDto);
        return ApiResponse.success("사용자 선호도 등록이 완료되었습니다.", "201", responseDto);
    }
}
