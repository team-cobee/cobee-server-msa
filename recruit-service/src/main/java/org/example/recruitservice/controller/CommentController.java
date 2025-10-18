package org.example.recruitservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.common.apiPayload.response.ApiResponse;
import org.example.recruitservice.domain.Comment;
import org.example.recruitservice.service.CommentService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("")
    public ApiResponse<Comment> addComment(@RequestBody Comment comment) {
        return null;
    }
}
