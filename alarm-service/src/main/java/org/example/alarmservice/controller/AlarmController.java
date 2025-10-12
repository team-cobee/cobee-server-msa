package org.example.alarmservice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alarm")
public class AlarmController {
    @GetMapping("")
    public ResponseEntity<String> alarm() {
        return ResponseEntity.ok("alarm Service on Network");
    }
}
