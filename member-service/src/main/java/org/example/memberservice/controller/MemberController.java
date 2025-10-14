package org.example.memberservice.controller;

import org.example.common.apiPayload.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MemberController {

    /**
     * OAuth2 로그인 성공 후, JWT 토큰을 화면에 표시하기 위한 테스트용 API입니다.
     * @param accessToken 로그인 성공 핸들러가 쿼리 파라미터로 전달한 JWT
     * @return accessToken을 포함한 JSON 응답
     * 브라우저 화면에 보여주기 위한 임시 코드입니다
     */
    @GetMapping("/login/success")
    public ResponseEntity<Map<String, String>> loginSuccess(@RequestParam("accessToken") String accessToken){
        // 받은 accessToken을 JSON 형태로 감싸서 브라우저에 보여줍니다.
        return ResponseEntity.ok(Map.of("accessToken", accessToken));
    }

}
