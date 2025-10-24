package org.example.recruitservice.service;

import lombok.RequiredArgsConstructor;
import org.example.common.apiPayload.error.exception.CustomException;
import org.example.common.apiPayload.response.ApiResponse;
import org.example.recruitservice.client.MemberClient;
import org.example.recruitservice.domain.Comment;
import org.example.recruitservice.domain.RecruitPost;
import org.example.recruitservice.dto.MemberCoreResponse;
import org.example.recruitservice.dto.comment.CommentRequest;
import org.example.recruitservice.dto.comment.CommentResponse;
import org.example.recruitservice.dto.converter.CommentConverter;
import org.example.recruitservice.repository.CommentRepository;
import org.example.recruitservice.repository.RecruitRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final RecruitRepository recruitRepository;
    private final MemberClient memberClient;

    public CommentResponse createComment(Long postId, CommentRequest request, Long memberId) {
        MemberCoreResponse member = memberClient.getMyInfo(memberId).getData();
        RecruitPost post = recruitRepository.findById(postId).orElseThrow();
        Comment parentComment = null;
        if (request.getParentId() != null) {
            parentComment = commentRepository.findById(request.getParentId()).orElseThrow();
        }
        Comment comment = Comment.builder()
                .commentAuthorId(member.getId())
                .content(request.getContent())
                .post(post)
                .isPrivate(request.getIsPrivate())
                .parent(parentComment)
                .build();

        if (parentComment != null) {
            parentComment.addChild(comment); // 대댓글일 경우 부모 댓글에 추가
        } else {
            post.addComment(comment); // 일반 댓글일 경우 게시글에 추가
        }

        commentRepository.save(comment);
        return CommentConverter.toCommentResponse(comment, member);


    }
}
