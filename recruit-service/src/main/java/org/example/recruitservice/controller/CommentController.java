package org.example.recruitservice.controller;

import lombok.RequiredArgsConstructor;
import org.example.common.apiPayload.response.ApiResponse;
import org.example.common.constant.GatewayConstant;
import org.example.recruitservice.domain.Comment;
import org.example.recruitservice.dto.comment.CommentRequest;
import org.example.recruitservice.dto.comment.CommentResponse;
import org.example.recruitservice.service.CommentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{postId}")
    public ApiResponse<CommentResponse> addComment(@PathVariable(name = "postId") Long postId, @RequestBody CommentRequest request,
                                                   @RequestHeader(GatewayConstant.GATEWAY_AUTH_HEADER) Long memberId) {
        return ApiResponse.success("댓글 생성 완료", "COMMENT-001", commentService.createComment(postId, request, memberId));
    }
}
