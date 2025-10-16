package org.example.memberservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.common.apiPayload.response.ApiResponse;
import org.example.common.constant.GatewayConstant;
import org.example.memberservice.service.OcrService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/ocr")
@RequiredArgsConstructor
public class OcrController {

    private final OcrService ocrService;

    @PostMapping("/verify")
    public ApiResponse<?> verifyOcr(
            @RequestHeader(GatewayConstant.GATEWAY_AUTH_HEADER) Long memberId,
            @RequestParam("image") MultipartFile imageFile
    ){
        boolean isSuccess = ocrService.verifyAndupdateOcrStatus(memberId, imageFile);

        if (isSuccess) {
            return ApiResponse.success("OCR 인증에 성공했습니다.", "200", null);
        } else {
            return ApiResponse.failure("OCR 인증에 실패했습니다.", "400", "정보가 일치하지 않습니다.");
        }
    }
}
