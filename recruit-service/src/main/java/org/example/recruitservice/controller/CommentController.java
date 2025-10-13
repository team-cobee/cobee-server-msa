package org.example.recruitservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.recruitservice.service.CommentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;
}
