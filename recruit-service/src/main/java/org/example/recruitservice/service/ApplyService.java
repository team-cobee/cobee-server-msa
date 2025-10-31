package org.example.recruitservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.common.domain.enums.AlarmSourceType;
import org.example.common.domain.enums.AlarmType;
import org.example.common.dto.alarm.CreateAlarmRequest;
import org.example.recruitservice.client.AlarmClient;
import org.example.recruitservice.domain.ApplyRecord;
import org.example.recruitservice.domain.Enum.MatchStatus;
import org.example.recruitservice.domain.RecruitPost;
import org.example.recruitservice.dto.apply.ApplicantResponse;
import org.example.recruitservice.dto.apply.ApplyAcceptRequest;
import org.example.recruitservice.dto.apply.ApplyResponse;
import org.example.recruitservice.dto.recruit.RecruitCoreResponse;
import org.example.recruitservice.dto.converter.ApplyConverter;
import org.example.recruitservice.dto.converter.RecruitConverter;
import org.example.recruitservice.repository.ApplyRepository;
import org.example.recruitservice.repository.RecruitRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ApplyService {
    private final ApplyRepository applyRepository;
    private final RecruitRepository recruitRepository;
    private final AlarmClient alarmClient;

    public ApplyResponse applyForRecruit(Long memberId, Long postId) {
        try {
            Optional<ApplyRecord> record = applyRepository.findApplyRecordsByAppliedMemberIdAndPostId(memberId, postId);
            if  (record.isPresent()) {
                throw new IllegalArgumentException("이미 지원하였습니다.");
            } else {
                RecruitPost recruitPost = recruitRepository.findById(postId).orElseThrow();
                ApplyRecord apply = ApplyRecord.builder()
                        .appliedMemberId(memberId)
                        .post(recruitPost)
                        .matchStatus(MatchStatus.ON_WAIT)
                        .submittedAt(LocalDate.now())
                        .build();
                applyRepository.save(apply);

                // Send "New Apply" notification
                CreateAlarmRequest alarmRequest = CreateAlarmRequest.builder()
                        .alarmType(AlarmType.NEW_APPLY)
                        .sourceType(AlarmSourceType.RECRUIT_POST)
                        .sourceId(postId)
                        .fromUserId(memberId)
                        .toUserId(recruitPost.getOwnerId())
                        .title("새로운 지원 알림")
                        .body("새로운 지원이 도착했습니다.")
                        .build();
                alarmClient.createAlarm(alarmRequest);

                return ApplyConverter.from(apply);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    public ApplyResponse acceptOrReject(ApplyAcceptRequest applyAccept) {
        try {
            ApplyRecord applyRecord = applyRepository.findById(applyAccept.getApplyId()).orElseThrow();
            Boolean accept = applyAccept.getIsAccepted();
            applyRecord.acceptMatching(accept);
            applyRepository.save(applyRecord);

            // Send "Matching Result" notification
            String title = accept ? "매칭 성공" : "매칭 거절";
            String body = accept ? "매칭이 성사되었습니다." : "매칭이 거절되었습니다.";
            RecruitPost post = applyRecord.getPost();

            CreateAlarmRequest matchResultAlarm = CreateAlarmRequest.builder()
                    .alarmType(AlarmType.START_MATCHING)
                    .sourceType(AlarmSourceType.RECRUIT_POST)
                    .sourceId(post.getId())
                    .fromUserId(post.getOwnerId()) // Post author
                    .toUserId(applyRecord.getAppliedMemberId()) // Applicant
                    .title(title)
                    .body(body)
                    .build();
            alarmClient.createAlarm(matchResultAlarm);

            // Send "Chatroom Invitation" notification if accepted
            if (accept) {
                CreateAlarmRequest invitationAlarm = CreateAlarmRequest.builder()
                        .alarmType(AlarmType.INVITED)
                        .sourceType(AlarmSourceType.CHATROOM)
                        .sourceId(applyRecord.getId()) // Temporary ID, should be chatRoomId
                        .fromUserId(post.getOwnerId())
                        .toUserId(applyRecord.getAppliedMemberId())
                        .title("채팅방 초대")
                        .body(post.getTitle() + " 채팅방에 초대되었어요.")
                        .build();
                alarmClient.createAlarm(invitationAlarm);
            }

            return ApplyConverter.from(applyRecord);
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    public List<ApplicantResponse> getMyApplicants(Long postId, Long memberId, MatchStatus matchStatus) {
        List<ApplyRecord> applyRecords = (matchStatus == null)
                ? applyRepository.findMyPostAppliersExceptMe(postId, memberId)
                : applyRepository.findMyPostAppliersExceptMeWithStatus(postId, memberId, matchStatus);
        return applyRecords.stream().map(ApplyConverter::applicantConverter).toList();
    }

    public List<RecruitCoreResponse> getMyAppliedPostsByMatchStatus(Long memberId, MatchStatus matchStatus ) {
        List<ApplyRecord> applyRecords = (matchStatus == null)
                ? applyRepository.findApplyRecordsByAppliedMemberId(memberId)
                : applyRepository.findApplyRecordsByAppliedMemberIdAndMatchStatus(memberId, matchStatus);
        return applyRecords.stream().map(RecruitConverter::fromApplyToRecruit).toList();
    }

    public Boolean checkIfIAppliedThisPost(Long postId, Long memberId) {
        Optional<ApplyRecord> apply = applyRepository.findApplyRecordsByAppliedMemberIdAndPostId(postId, memberId);
        return apply.isPresent();
    }

    public void deleteAllApplyData(Long memberId) {
        List<ApplyRecord> records = applyRepository.findApplyRecordsByAppliedMemberId(memberId);
        applyRepository.deleteAll(records);
    }
}
