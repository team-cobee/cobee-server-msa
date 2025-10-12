package org.example.recruitservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.recruitservice.domain.ApplyRecord;
import org.example.recruitservice.domain.MatchStatus;
import org.example.recruitservice.domain.RecruitPost;
import org.example.recruitservice.dto.ApplyResponse;
import org.example.recruitservice.dto.converter.ApplyConverter;
import org.example.recruitservice.repository.ApplyRepository;
import org.example.recruitservice.repository.RecruitRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ApplyService {
    private final ApplyRepository applyRepository;
    private final RecruitRepository recruitRepository;

    public ApplyResponse applyForRecruit(Long memberId, Long postId) {
        try {
            RecruitPost recruitPost = recruitRepository.findById(postId).orElseThrow();
            ApplyRecord apply = ApplyRecord.builder()
                    .appliedMemberId(memberId)
                    .post(recruitPost)
                    .matchStatus(MatchStatus.ON_WAIT)
                    .submittedAt(LocalDate.now())
                    .build();
            applyRepository.save(apply);
            return ApplyConverter.from(apply);
        } catch (Exception e) {
            log.info(e.getMessage());
            return null;
        }
    }
}
