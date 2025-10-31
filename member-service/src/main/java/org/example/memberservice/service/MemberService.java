package org.example.memberservice.service;

import lombok.RequiredArgsConstructor;
import org.example.common.nhn.NhnStorageService;
import org.example.memberservice.domain.Member;
import org.example.memberservice.dto.member.MemberResponseDto;
import org.example.memberservice.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final NhnStorageService nhnStorageService;

    @Transactional(readOnly = true)
    public MemberResponseDto getMyInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        return MemberResponseDto.from(member);
    }

    // 4. 아래 메서드 추가
    @Transactional
    public String updateProfileImage(Long memberId, MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
            // 또는 ImageValidationUtil을 공통 모듈로 옮겨서 사용
            // ImageValidationUtil.validateSingleImageFile(file);
        }

        // 1. NHN Storage에 파일 업로드 (비동기 Mono를 동기방식으로 변경)
        String imageUrl = nhnStorageService.uploadFile(file).block();

        if (imageUrl == null) {
            throw new RuntimeException("NHN 스토리지 업로드에 실패했습니다.");
        }

        // 2. Member 엔티티에 URL 저장
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // 3. 제공해주신 Member 엔티티의 update 메서드 사용
        member.update(member.getName(), imageUrl);
        return imageUrl;
    }
}