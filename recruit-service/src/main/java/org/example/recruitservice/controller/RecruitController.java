package org.example.recruitservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.recruitservice.dto.RecruitRequest;
import org.example.recruitservice.dto.RecruitResponse;
import org.example.recruitservice.service.RecruitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recruit")
@RequiredArgsConstructor
public class RecruitController {
    private final RecruitService recruitService;

    @GetMapping("")
    public ResponseEntity<String> recruit() {
        return ResponseEntity.ok("Hello World This is Recruit");
    }

    @PostMapping("")
    public ResponseEntity<RecruitResponse> recruit(@RequestBody RecruitRequest recruitRequest) {
        return ResponseEntity.ok(recruitService.createRecruitPost(recruitRequest));
    }
}
