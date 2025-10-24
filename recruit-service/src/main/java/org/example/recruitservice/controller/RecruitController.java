package org.example.recruitservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.common.apiPayload.response.ApiResponse;
import org.example.common.constant.GatewayConstant;
import org.example.recruitservice.dto.RecruitCoreResponse;
import org.example.recruitservice.dto.RecruitRequest;
import org.example.recruitservice.dto.RecruitResponse;
import org.example.recruitservice.service.ApplyService;
import org.example.recruitservice.service.RecruitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recruit")
@RequiredArgsConstructor
public class RecruitController {
    private final RecruitService recruitService;
    private final ApplyService applyService;

    @PostMapping("")
    public ApiResponse<RecruitResponse> createRecruit(@RequestBody RecruitRequest recruitRequest,
                                                      @RequestHeader(GatewayConstant.GATEWAY_AUTH_HEADER) Long memberId) {
        return ApiResponse.success("구인글 생성 완료", "RECRUIT-001", recruitService.createRecruitPost(recruitRequest, memberId));
    }

    @PatchMapping("/{postId}")
    public ApiResponse<RecruitResponse> updateRecruit(@PathVariable("postId") Long postId, @RequestBody RecruitRequest recruitRequest) {
        return ApiResponse.success("구인글 수정 완료", "RECRUIT-002", recruitService.updateRecruit(postId,recruitRequest));
    }

    @GetMapping("/{postId}")
    public ApiResponse<RecruitResponse> getRecruitInfo(@PathVariable("postId") Long postId) {
        return ApiResponse.success("구인글 조회 완료", "RECRUIT-003", recruitService.getOneRecruitInfo(postId));
    }

    @GetMapping("")
    public ApiResponse<List<RecruitCoreResponse>> getAllRecruitsInfo() {
        return ApiResponse.success("모든 구인글 조회 완료", "RECRUIT-004",recruitService.getAllRecruitInfo());
    }

    @GetMapping("/my")
    public ApiResponse<List<RecruitCoreResponse>> getMyRecruitPostsInfo(@RequestParam Long userId) {
        return ApiResponse.success("나의 구인글 모두 조회 완료", "RECRUIT-005",recruitService.getMyAllRecruitInfo(userId));
    }

    @DeleteMapping("/{postId}")
    public ApiResponse<String> deleteRecruit(@PathVariable("postId") Long postId) {
        return ApiResponse.success("구인글 삭제하기 완료", "RECRUIT-006", recruitService.deleteRecruitPost(postId));
    }


    @DeleteMapping("/by-member/{memberId}")
    public ResponseEntity<Void> deleteAllRecruitDataByMemberId(@PathVariable("memberId") Long memberId){
        recruitService.deleteAllRecruitData(memberId);
        applyService.deleteAllApplyData(memberId);
        return ResponseEntity.ok().build();
    }
}
