package org.example.recruitservice.dto;

import lombok.Getter;

@Getter
public class ApplyAcceptRequest {
    private Boolean isAccepted;
    private Long applyId;
}
