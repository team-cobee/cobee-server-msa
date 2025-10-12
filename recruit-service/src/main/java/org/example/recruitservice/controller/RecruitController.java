package org.example.recruitservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.recruitservice.dto.RecruitRequest;
import org.example.recruitservice.dto.RecruitResponse;
import org.example.recruitservice.service.RecruitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recruit")
@RequiredArgsConstructor
public class RecruitController {
    private final RecruitService recruitService;

    @GetMapping("/test")
    public ResponseEntity<String> recruit() {
        return ResponseEntity.ok("Hello World This is Recruit");
    }

    @PostMapping("")
    public ResponseEntity<RecruitResponse> recruit(@RequestBody RecruitRequest recruitRequest) {
        return ResponseEntity.ok(recruitService.createRecruitPost(recruitRequest));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<RecruitResponse> recruit(@PathVariable("postId") Long postId, @RequestBody RecruitRequest recruitRequest) {
        return ResponseEntity.ok(recruitService.updateRecruit(postId,recruitRequest));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<RecruitResponse> recruit(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(recruitService.getOneRecruitInfo(postId));
    }

    @GetMapping("")
    public ResponseEntity<List<RecruitResponse>> getAllRecruits() {
        return ResponseEntity.ok(recruitService.getAllRecruitInfo());
    }

    @GetMapping("/my")
    public ResponseEntity<List<RecruitResponse>> getMyRecruits(@RequestParam Long userId) {
        return ResponseEntity.ok(recruitService.getMyAllRecruitInfo(userId));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deleteRecruit(@PathVariable("postId") Long postId) {
        return ResponseEntity.ok(recruitService.deleteRecruitPost(postId));
    }

}
