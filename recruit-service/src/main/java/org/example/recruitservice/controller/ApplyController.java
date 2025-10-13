package org.example.recruitservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.recruitservice.domain.ApplyRecord;
import org.example.recruitservice.dto.ApplicantResponse;
import org.example.recruitservice.dto.ApplyResponse;
import org.example.recruitservice.service.ApplyAcceptRequest;
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

    @GetMapping("/applier/{postId}/{memberId}")
    public ResponseEntity<List<ApplicantResponse>> getMyAppliers(@PathVariable Long postId, @PathVariable Long memberId) {
        // 이때 memberId는 로그인한 사용자 본인 - 내가 수락할 지원자 정보
        return ResponseEntity.ok(applyService.getMyApplicants(postId, memberId));
    }
}
