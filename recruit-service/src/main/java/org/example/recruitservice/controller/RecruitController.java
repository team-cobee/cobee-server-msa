package org.example.recruitservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recruit")
public class RecruitController {
    @GetMapping
    public ResponseEntity<String> recruit() {
        return ResponseEntity.ok("Hello World This is Recruit");
    }
}
