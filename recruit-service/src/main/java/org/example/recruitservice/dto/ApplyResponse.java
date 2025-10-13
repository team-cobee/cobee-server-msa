package org.example.recruitservice.dto;

import lombok.Builder;
import lombok.Getter;
import org.example.recruitservice.domain.Enum.MatchStatus;

@Getter
@Builder
public class ApplyResponse {
    private Long id;
    private Long appliedPost;
    private Long applierId;
    private MatchStatus matchStatus;
}
