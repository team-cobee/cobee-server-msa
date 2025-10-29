package org.example.memberservice.service;

import lombok.RequiredArgsConstructor;
import org.example.memberservice.domain.Member;
import org.example.memberservice.dto.member.MemberResponseDto;
import org.example.memberservice.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberResponseDto getMyInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        return MemberResponseDto.from(member);
    }
}
