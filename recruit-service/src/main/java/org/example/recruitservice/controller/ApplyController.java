package org.example.recruitservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.recruitservice.domain.Enum.MatchStatus;
import org.example.recruitservice.dto.ApplicantResponse;
import org.example.recruitservice.dto.ApplyResponse;
import org.example.recruitservice.dto.RecruitCoreResponse;
import org.example.recruitservice.dto.ApplyAcceptRequest;
import org.example.recruitservice.service.ApplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/apply")
@RequiredArgsConstructor
public class ApplyController {
    private final ApplyService applyService;

    // TODO : MemberDTO 받으면 로그인 멤버 정보 추가하기
    @PostMapping("/{postId}/{applier}")
    public ResponseEntity<ApplyResponse> apply(@PathVariable Long applier, @PathVariable Long postId) {
        return ResponseEntity.ok(applyService.applyForRecruit(applier, postId));
    }

    @PostMapping("/accept")
    public ResponseEntity<ApplyResponse> accept(@RequestBody ApplyAcceptRequest applyRecord) {
        return ResponseEntity.ok(applyService.acceptOrReject(applyRecord));
    }

    // 특정 구인글에서의 지원자 조회(내가 수락할 지원자 정보) + 상태에 따른 조회
    @GetMapping("/applier/{postId}/{memberId}")
    public ResponseEntity<List<ApplicantResponse>> getMyAppliers(
            @PathVariable Long postId, @PathVariable Long memberId,
            @RequestParam(name = "status", required = false) MatchStatus status) {
        // 이때 memberId는 로그인한 사용자 본인
        return ResponseEntity.ok(applyService.getMyApplicants(postId, memberId, status));
    }

    @GetMapping("/my")  // 나의 매칭 상태에 따른 지원 구인글 조회 - Long memberId는 본인
    public ResponseEntity<List<RecruitCoreResponse>> getMyAppliedPostsInfo(@RequestParam Long memberId,
                                                                           @RequestParam(name = "status", required = false) MatchStatus status) {
        return ResponseEntity.ok(applyService.getMyAppliedPostsByMatchStatus(memberId, status));
    }

    @GetMapping("/isApplied/{postId}") // 내가 해당 구인글에 지원했는지 여부
    public ResponseEntity<Boolean> checkIfApplied(@RequestParam Long memberId,
                                                  @PathVariable(name="postId") Long postId){
            return ResponseEntity.ok(applyService.checkIfIAppliedThisPost(postId, memberId));
    }

}
