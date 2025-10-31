package org.example.recruitservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    public ApplyResponse applyForRecruit(Long memberId, Long postId) {
        try {
            Optional<ApplyRecord> record = applyRepository.findApplyRecordsByAppliedMemberIdAndPostId(memberId, postId);
            if  (record.isPresent()) {
                throw new IllegalArgumentException("이미 지원하였습니다.");  // custom exception - 이미 지원하였다고 경고주고 지원못하게 하기
            } else {
                RecruitPost recruitPost = recruitRepository.findById(postId).orElseThrow();
                ApplyRecord apply = ApplyRecord.builder()
                        .appliedMemberId(memberId)
                        .post(recruitPost)
                        .matchStatus(MatchStatus.ON_WAIT)
                        .submittedAt(LocalDate.now())
                        .build();
                applyRepository.save(apply);
                return ApplyConverter.from(apply);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }

    public ApplyResponse acceptOrReject(ApplyAcceptRequest applyAccept) {
        // 누가 하는지에 대한거 검증 멤버 아이디 받은 이후로 설정하기?
        try {
            ApplyRecord applyRecord = applyRepository.findById(applyAccept.getApplyId()).orElseThrow();
            //Long checkAuthor = applyRecord.getAppliedMemberId();
            //if (memberId.equals(checkAuthor)) {
            Boolean accept = applyAccept.getIsAccepted();
            applyRecord.acceptMatching(accept);
            applyRepository.save(applyRecord);
            //}
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
        // 해당 멤버의 모든 지원 기록을 찾아서 삭제
        List<ApplyRecord> records = applyRepository.findApplyRecordsByAppliedMemberId(memberId);
        applyRepository.deleteAll(records);
    }

//    public void sendInvitationAlarm(ApplyRecord applyRecord) {
//        try {
//            RecruitPost post = applyRecord.getPost();
//            CreateAlarmRequest request = CreateAlarmRequest.builder()
//                    .alarmType(AlarmType.INVITED)
//                    .sourceType(AlarmSourceType.APPLY_RESULT)
//                    .sourceId(post.getId())
//                    .fromUserId(post.getOwnerId())
//                    .toUserId(applyRecord.getAppliedMemberId())
//                    .title("지원이 수락되었어요")
//                    .body(String.format("%s 구인글 지원이 수락되었습니다. 채팅방에서 대화를 시작해보세요.", post.getTitle()))
//                    .data(Map.of(
//                            "postId", String.valueOf(post.getId()),
//                            "applyId", String.valueOf(applyRecord.getId()),
//                            "matchStatus", applyRecord.getMatchStatus().name()
//                    ))
//                    .build();
//            alarmClient.createAlarm(request);
//        } catch (Exception alarmError) {
//            log.warn("Failed to create invitation alarm for applyRecord {}: {}", applyRecord.getId(), alarmError.getMessage());
//        }
//    }
}
