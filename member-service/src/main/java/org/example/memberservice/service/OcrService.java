package org.example.memberservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.memberservice.domain.Member;
import org.example.memberservice.dto.OcrValueDto;
import org.example.memberservice.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OcrService {
    private final MemberRepository memberRepository;

    @Transactional
    public void verifyAndUpdateOcrStatus(Long memberId, OcrValueDto ocrResult){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        String ocrName = ocrResult.findValueByKey("name");
        String ocrResidentNumber = ocrResult.findValueByKey("residentNumber");

        // 1. 이름 비교
        if (!member.getName().equals(ocrName)) {
            log.warn("OCR 이름 불일치. DB: {}, OCR: {}", member.getName(), ocrName);
            throw new RuntimeException("Member name not match");
        }

        // 2. 생년월일 및 성별 처리
        String ocrBirthDate = parseBirthDate(ocrResidentNumber); // "YYMMDD"
        String ocrGender = parseGender(ocrResidentNumber);       // "MALE" or "FEMALE"

        // 생년월일 처리

        // 성별 처리

        return;
    }

    private String parseGender(String ocrResidentNumber) {

        return null;
    }

    private String parseBirthDate(String ocrResidentNumber) {
        String o = null;
        return o;
    }

}
