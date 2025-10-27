package org.example.memberservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.memberservice.domain.Member;
import org.example.memberservice.domain.MemberPreference;
import org.example.memberservice.dto.MemberPreferencesRequestDto;
import org.example.memberservice.dto.MemberPreferencesResponseDto;
import org.example.memberservice.repository.MemberPreferenceRepository;
import org.example.memberservice.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MemberPreferenceService {
    private final MemberPreferenceRepository memberPreferenceRepository;
    private final MemberRepository memberRepository;

    public MemberPreferencesResponseDto createMemberPreferences(Long memberId, MemberPreferencesRequestDto requestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        if (memberPreferenceRepository.existsByMemberId(memberId)) {
            throw new RuntimeException("Member already exists");
        }
        MemberPreference memberPreference = MemberPreference.builder()
                .gender(requestDto.getPreferredGender())
                .lifestyle(requestDto.getLifestyle())
                .personality(requestDto.getPersonality())
                .isSmoking(requestDto.getSmokingPreference())
                .isSnoring(requestDto.getSnoringPreference())
                .hasPet(requestDto.getPetPreference())
                .cohabitantCount(requestDto.getCohabitantCount())
                .preferredAgeMin(requestDto.getPreferredAgeMin())
                .preferredAgeMax(requestDto.getPreferredAgeMax())
                .build();
        MemberPreference savedPreferences = memberPreferenceRepository.save(memberPreference);
        log.info("사용자 선호도 등록 완료 - memberId: {}, preferencesId: {}", memberId, savedPreferences.getId());
        return MemberPreferencesResponseDto.from(savedPreferences);
    }
}
