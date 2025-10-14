package org.example.memberservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.common.apiPayload.response.ApiResponse;
import org.example.common.constant.GatewayConstant;
import org.example.memberservice.dto.MemberResponseDto;
import org.example.memberservice.service.MemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    /**
     * OAuth2 로그인 성공 후, JWT 토큰을 화면에 표시하기 위한 테스트용 API입니다.
     * @param accessToken 로그인 성공 핸들러가 쿼리 파라미터로 전달한 JWT
     * @return accessToken을 포함한 JSON 응답
     * 브라우저 화면에 보여주기 위한 임시 코드입니다
     */
/*    @GetMapping("/login/success")
    public ResponseEntity<Map<String, String>> loginSuccess(@RequestParam("accessToken") String accessToken){
        // 받은 accessToken을 JSON 형태로 감싸서 브라우저에 보여줍니다.
        return ResponseEntity.ok(Map.of("accessToken", accessToken));
    }*/

    /**
     * 인가 테스트 API
     */
    @GetMapping("/members/me")
    public ApiResponse<MemberResponseDto> getMyInfo(@RequestHeader(GatewayConstant.GATEWAY_AUTH_HEADER) Long memberId) {
        MemberResponseDto myInfo = memberService.getMyInfo(memberId);
        return ApiResponse.success("내 정보 조회 성공", "200", myInfo);
    }
}
