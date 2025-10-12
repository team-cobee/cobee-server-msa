package org.example.recruitservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.recruitservice.repository.RecruitRepository;
import org.example.recruitservice.domain.Enum.RecruitStatus;
import org.example.recruitservice.domain.RecruitPost;
import org.example.recruitservice.dto.RecruitRequest;
import org.example.recruitservice.dto.RecruitResponse;
import org.example.recruitservice.dto.converter.RecruitConverter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RecruitService {
    private final RecruitRepository recruitRepository;

    public RecruitResponse createRecruitPost(RecruitRequest recruitPost/*, Long memberId */) {
        RecruitPost recruit = RecruitPost.builder()
                //.ownerId(memberId)
                .title(recruitPost.getTitle())
                .recruitCount(recruitPost.getRecruitCount())
                .rentCostMin(recruitPost.getRentCostMin())
                .rentCostMax(recruitPost.getRentCostMax())
                .monthlyCostMin(recruitPost.getMonthlyCostMin())
                .monthlyCostMax(recruitPost.getMonthlyCostMax())
                .minAge(recruitPost.getMinAge())
                .maxAge(recruitPost.getMaxAge())
                .preferredGender(recruitPost.getGender())
                .lifeStyle(recruitPost.getLifestyle())
                .personality(recruitPost.getPersonality())
                .isSmoking(recruitPost.getIsSmoking())
                .isSnoring(recruitPost.getIsSnoring())
                .hasPet(recruitPost.getHasPet())
                .hasRoom(recruitPost.getHasRoom())
                .address(recruitPost.getAddress())
                .detailDescription(recruitPost.getDetailDescription())
                .additionalDescription(recruitPost.getAdditionalDescription())
                .status(RecruitStatus.RECRUITING) // 구인글 등록하자마자 구인중으로 변경
                .build();

        // 위도 경도 저장 - location api에서 가져와서 저장하기
        recruitRepository.save(recruit);
        return RecruitConverter.baseResponse(recruit);

    }

    public RecruitResponse updateRecruit(Long postId, RecruitRequest recruitRequest) {
        RecruitPost post = recruitRepository.findById(postId).orElseThrow();  // 에러 처리도 해야하는데...
        post.updatePost(recruitRequest);
        recruitRepository.save(post);
        return RecruitConverter.baseResponse(post);
    }

    public RecruitResponse getOneRecruitInfo(Long postId) {
        RecruitPost post = recruitRepository.findById(postId).orElseThrow();
        return RecruitConverter.baseResponse(post);
    }

    public List<RecruitResponse> getAllRecruitInfo() {
        List<RecruitPost> recruits = recruitRepository.findAll();
        return recruits.stream().map(RecruitConverter::baseResponse).toList();
    }

    public List<RecruitResponse> getMyAllRecruitInfo(Long userId) {
        List<RecruitPost> recruits = recruitRepository.findAllByOwnerId(userId);
        return recruits.stream().map(RecruitConverter::baseResponse).toList();
    }

    public String deleteRecruitPost(Long postId) {
        try{
            recruitRepository.findById(postId).orElseThrow();  // 경우에 따른 에러 메시지 및 응답 틀 필요할듯 + 작성자가 본인인지 확인도 해야함 고민..
            recruitRepository.deleteById(postId);
            return "구인글 id가 "+postId+"인 구인글 삭제 성공";
        } catch (Exception e){
            return "구인글 삭제 불가능";
        }
    }
}
