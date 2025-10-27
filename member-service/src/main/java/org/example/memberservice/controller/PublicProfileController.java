package org.example.memberservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.apiPayload.error.exception.CustomException;
import org.example.common.apiPayload.response.ApiResponse;
import org.example.memberservice.dto.PublicProfileRequestDto;
import org.example.memberservice.dto.PublicProfileResponseDto;
import org.example.memberservice.dto.PublicProfileUpdateRequestDto;
import org.example.memberservice.service.PublicProfileService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public-profiles")
@Slf4j
public class PublicProfileController {

    private final PublicProfileService publicProfileService;

    @PostMapping("/{memberId}")
    public ApiResponse<Void> createPublicProfile(
            //@RequestHeader(GatewayConstant.GATEWAY_AUTH_HEADER) Long memberId,
            @PathVariable Long memberId,
            @RequestBody PublicProfileRequestDto requestDto) {
        try {
            publicProfileService.createPublicProfile(memberId, requestDto);
            return ApiResponse.success("Public profile created successfully", "201");
        } catch (IllegalArgumentException e) {
            return ApiResponse.failure("요청이 유효하지 않음", "400", e.getMessage());
        } catch (Exception e) {
            log.info(e.getMessage());
            return ApiResponse.failure("서버 내부 에러", "500", e.getMessage());
        }
    }

    @GetMapping("/{memberId}")
    public ApiResponse<PublicProfileResponseDto> getPublicProfile(
            //@RequestHeader(GatewayConstant.GATEWAY_AUTH_HEADER) Long memberId
            @PathVariable Long memberId) {
        PublicProfileResponseDto profile = publicProfileService.getPublicProfile(memberId);
        return ApiResponse.success("Public profile retrieved successfully", "200", profile);
    }

    @PatchMapping("/{memberId}")
    public ApiResponse<Void> updatePublicProfile(
            //@RequestHeader(GatewayConstant.GATEWAY_AUTH_HEADER) Long memberId,
            @PathVariable Long memberId,
            @RequestBody PublicProfileUpdateRequestDto requestDto) {
        publicProfileService.updatePublicProfile(memberId, requestDto);
        return ApiResponse.success("Public profile modified successfully", "200");
    }

    @DeleteMapping("/{memberId}")
    public ApiResponse<Void> deletePublicProfile(
            //@RequestHeader(GatewayConstant.GATEWAY_AUTH_HEADER) Long memberId,
            @PathVariable Long memberId
    ) {
        publicProfileService.deletePublicProfile(memberId);
        return ApiResponse.success("Public profile deleted successfully", "200");
    }

    @GetMapping("/p-id/{publicProfileId}")
    public ApiResponse<PublicProfileResponseDto> getPublicProfileById(
            //@RequestHeader(GatewayConstant.GATEWAY_AUTH_HEADER) Long memberId,
            @PathVariable Long publicProfileId
    ) {
        try {
            PublicProfileResponseDto profile = publicProfileService.getPublicProfileById(publicProfileId);
            return ApiResponse.success("Public profile retrieved successfully", "200", profile);
        } catch (CustomException e) {
            return ApiResponse.failure("공개프로필을 찾을 수 없음", "404", e.getMessage());
        } catch (Exception e) {
            log.info(e.getMessage());
            return ApiResponse.failure("서버 내부 에러", "500", e.getMessage());
        }
    }
}

