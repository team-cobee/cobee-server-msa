package org.example.recruitservice.dto.converter;

import lombok.Builder;
import org.example.recruitservice.domain.ApplyRecord;
import org.example.recruitservice.dto.ApplyResponse;

@Builder
public class ApplyConverter {
    public static ApplyResponse from(ApplyRecord apply) {
        return ApplyResponse.builder()
                .id(apply.getId())
                .appliedPost(apply.getPost().getId())
                .applierId(apply.getAppliedMemberId())
                .matchStatus(apply.getMatchStatus())
                .build();
    }
}
