package org.example.recruitservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.common.apiPayload.response.ApiResponse;
import org.example.recruitservice.client.MemberClient;
import org.example.recruitservice.dto.MemberCoreResponse;
import org.example.recruitservice.dto.recruit.RecruitCoreResponse;
import org.example.recruitservice.repository.RecruitRepository;
import org.example.recruitservice.domain.Enum.RecruitStatus;
import org.example.recruitservice.domain.RecruitPost;
import org.example.recruitservice.dto.recruit.RecruitRequest;
import org.example.recruitservice.dto.recruit.RecruitResponse;
import org.example.recruitservice.dto.converter.RecruitConverter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class RecruitService {
    private final RecruitRepository recruitRepository;
    private final MemberClient memberClient;
    private final GoogleMapService googleMapService;

    public RecruitResponse createRecruitPost(RecruitRequest recruitPost, Long memberId) {
        ApiResponse<MemberCoreResponse> memberInfoResponse = memberClient.getMyInfo(memberId);
        Map<String, Object> geocodeData = googleMapService.getGeocode(recruitPost.getAddress());
        double latitude = (double) geocodeData.get("latitude");
        double longitude = (double) geocodeData.get("longitude");
        String formattedAddress = (String) geocodeData.get("formattedAddress");

        MemberCoreResponse memberInfo;
        if (memberInfoResponse != null && memberInfoResponse.isSuccess() && memberInfoResponse.getData() != null) {
            memberInfo = memberInfoResponse.getData();
        } else {
            memberInfo = MemberCoreResponse.builder()
                    .email(memberInfoResponse.getData().getEmail())
                    .name(memberInfoResponse.getData().getName())
                    .id(memberInfoResponse.getData().getId())
                    .build();
        }
        Long ownerId = memberInfo.getId();

        RecruitPost recruit = RecruitPost.builder()
                .ownerId(ownerId)
                .ownerName(Objects.requireNonNull(memberInfoResponse).getData().getName())
                .ownerEmail(memberInfoResponse.getData().getEmail())
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
                .address(formattedAddress)
                .regionLatitude(latitude)
                .regionLongitude(longitude)
                .detailDescription(recruitPost.getDetailDescription())
                .additionalDescription(recruitPost.getAdditionalDescription())
                .status(RecruitStatus.RECRUITING) // 구인글 등록하자마자 구인중으로 변경
                .build();

        // 위도 경도 저장 - location api에서 가져와서 저장하기
        recruitRepository.save(recruit);
        return RecruitConverter.from(recruit);

    }

    public RecruitResponse updateRecruit(Long postId, RecruitRequest recruitRequest) {
        RecruitPost post = recruitRepository.findById(postId).orElseThrow();  // 에러 처리도 해야하는데...
        Map<String, Object> geocodeData = null;
        if (recruitRequest.getAddress() != null && !recruitRequest.getAddress().equals(post.getAddress())) {
            geocodeData = googleMapService.getGeocode(recruitRequest.getAddress());
        }
        post.updatePost(recruitRequest, geocodeData);
        recruitRepository.save(post);
        return RecruitConverter.from(post);
    }

    public RecruitResponse getOneRecruitInfo(Long postId) {
        RecruitPost post = recruitRepository.findById(postId).orElseThrow();
        return RecruitConverter.from(post);
    }

    public List<RecruitCoreResponse> getAllRecruitInfo() {
        List<RecruitPost> recruits = recruitRepository.findAll();
        return recruits.stream().map(RecruitConverter::fromRecruit).toList();
    }

    public List<RecruitCoreResponse> getMyAllRecruitInfo(Long userId) {
        List<RecruitPost> recruits = recruitRepository.findAllByOwnerId(userId);
        return recruits.stream().map(RecruitConverter::fromRecruit).toList();
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

    public void deleteAllRecruitData(Long memberId) {
        // 해당 멤버가 작성한 모든 구인글을 찾아서 삭제
        List<RecruitPost> posts = recruitRepository.findAllByOwnerId(memberId);
        recruitRepository.deleteAll(posts);
    }
}
