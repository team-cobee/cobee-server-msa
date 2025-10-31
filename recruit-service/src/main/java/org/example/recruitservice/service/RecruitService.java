package org.example.recruitservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.common.apiPayload.response.ApiResponse;
import org.example.common.nhn.NhnStorageService;
import org.example.recruitservice.client.MemberClient;
import org.example.recruitservice.domain.Images;
import org.example.recruitservice.dto.MemberCoreResponse;
import org.example.recruitservice.dto.map.RecruitMapFilterResponse;
import org.example.recruitservice.dto.recruit.RecruitCoreResponse;
import org.example.recruitservice.repository.ImagesRepository;
import org.example.recruitservice.repository.RecruitRepository;
import org.example.recruitservice.domain.Enum.RecruitStatus;
import org.example.recruitservice.domain.RecruitPost;
import org.example.recruitservice.dto.recruit.RecruitRequest;
import org.example.recruitservice.dto.recruit.RecruitResponse;
import org.example.recruitservice.dto.converter.RecruitConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RecruitService {
    private final RecruitRepository recruitRepository;
    private final MemberClient memberClient;
    private final GoogleMapService googleMapService;
    private final NhnStorageService nhnStorageService;
    private final ImagesRepository imagesRepository;

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

    public List<RecruitMapFilterResponse> getfilterRecruitPosts(Double latitude, Double longitude, Double radius,
                                                                Integer recruitCount, Integer rentCostMin, Integer rentCostMax,
                                                                Integer monthlyCostMin, Integer monthlyCostMax) {
        List<RecruitPost> posts;

        if (latitude != null && longitude != null && radius != null) {
            posts = recruitRepository.findFilteredRecruitPosts(latitude, longitude, radius, recruitCount, rentCostMin,
                    rentCostMax, monthlyCostMin, monthlyCostMax);
        } else {
            posts = recruitRepository.findFilteredRecruitPosts(null, null, null, recruitCount, rentCostMin, rentCostMax,
                    monthlyCostMin, monthlyCostMax);
        }
        return posts.stream()
                .map(RecruitMapFilterResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<String> addImages(Long postId, Long memberId, MultipartFile[] files) {
        if (files == null || files.length == 0) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        RecruitPost post = recruitRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("구인글을 찾을 수 없습니다."));

        // 본인 확인
        if (!post.getOwnerId().equals(memberId)) {
            // (공통 모듈의 CustomException을 사용하시는 것이 좋습니다)
            throw new RuntimeException("본인의 구인글에만 이미지를 추가할 수 있습니다.");
        }

        List<String> imageUrls = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            if (file == null || file.isEmpty()) continue;

            // 1. NHN Storage에 파일 업로드
            String imageUrl = nhnStorageService.uploadFile(file).block();

            if (imageUrl != null) {
                // 2. Images 엔티티 생성 및 저장
                Images image = Images.builder()
                        .imageUrl(imageUrl)
                        .originalName(file.getOriginalFilename())
                        .displayOrder(i + 1)
                        .recruitPost(post)
                        .build();
                imagesRepository.save(image);
                imageUrls.add(imageUrl);
            }
        }
        return imageUrls;
    }
}
