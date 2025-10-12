package org.example.recruitservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.recruitservice.domain.ApplyRecord;
import org.example.recruitservice.dto.ApplyResponse;
import org.example.recruitservice.service.ApplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apply")
@RequiredArgsConstructor
public class ApplyController {
    private final ApplyService applyService;

    @PostMapping("/{postId}")
    public ResponseEntity<ApplyResponse> apply(@PathVariable Long postId) {

    }
}
