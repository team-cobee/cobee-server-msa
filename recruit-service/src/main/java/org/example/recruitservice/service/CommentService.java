package org.example.recruitservice.service;

import lombok.RequiredArgsConstructor;
import org.example.common.apiPayload.error.exception.CustomException;
import org.example.common.apiPayload.response.ApiResponse;
import org.example.common.domain.enums.AlarmSourceType;
import org.example.common.domain.enums.AlarmType;
import org.example.common.dto.alarm.CreateAlarmRequest;
import org.example.recruitservice.client.AlarmClient;
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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final RecruitRepository recruitRepository;
    private final MemberClient memberClient;
    private final AlarmClient alarmClient;

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

        commentRepository.save(comment);

        // Send notification
        boolean isReply = parentComment != null;
        String title = isReply ? "대댓글 알림" : "새 댓글 알림";
        String body = isReply ? "내 댓글에 답글이 달렸어요" : "내 글에 댓글이 달렸어요";
        Long toUserId = isReply ? parentComment.getCommentAuthorId() : post.getOwnerId();

        // Prevent self-notification
        if (!memberId.equals(toUserId)) {
            CreateAlarmRequest alarmRequest = CreateAlarmRequest.builder()
                    .alarmType(AlarmType.COMMENT)
                    .sourceType(AlarmSourceType.COMMENT)
                    .sourceId(comment.getId())
                    .fromUserId(memberId)
                    .toUserId(toUserId)
                    .title(title)
                    .body(body)
                    .build();
            alarmClient.createAlarm(alarmRequest);
        }

        return CommentConverter.toCommentResponse(comment, member);
    }

    public List<CommentResponse> getAllCommentsInfo(Long postId) {
        List<Comment> comments = commentRepository.findCommentsByPost_Id(postId);
        List<CommentResponse> commentResponses = new ArrayList<>();
        for (Comment comment : comments) {
            commentResponses.add(CommentConverter.toCommentResponse(comment, memberClient.getMyInfo(comment.getCommentAuthorId()).getData()));
        }
        return commentResponses;
    }
}
