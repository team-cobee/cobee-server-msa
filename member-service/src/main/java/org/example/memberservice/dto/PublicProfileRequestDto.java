package org.example.memberservice.dto;

import org.example.memberservice.domain.enums.LifeStyle;
import org.example.memberservice.domain.enums.Personality;

public record PublicProfileRequestDto(
        LifeStyle lifeStyle,
        Personality personality,
        Boolean isSmoking,
        Boolean isSnoring,
        Boolean hasPet,
        String myInfo
) {
}
