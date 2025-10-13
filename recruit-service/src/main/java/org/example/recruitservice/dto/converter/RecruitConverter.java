package org.example.recruitservice.dto.converter;

import lombok.Builder;
import org.example.recruitservice.domain.ApplyRecord;
import org.example.recruitservice.domain.RecruitPost;
import org.example.recruitservice.dto.RecruitCoreResponse;
import org.example.recruitservice.dto.RecruitResponse;

import java.util.List;

@Builder
public class RecruitConverter {

    public static RecruitResponse baseResponse(RecruitPost post) {

        return RecruitResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .address(post.getAddress())
                .viewed(0)
                .bookmarked(0)
                .status(post.getStatus())

//                .authorId(post.getMember().getId())
//                .authorName(post.getMember().getName())
//                .authorGender(Gender.valueOf(post.getMember().getGender()))
//                .birthdate(post.getMember().getBirthDate())

                .recruitCount(post.getRecruitCount())
                .hasRoom(post.getHasRoom())
                .rentalCostMin(post.getRentCostMin())
                .rentalCostMax(post.getRentCostMax())
                .monthlyCostMin(post.getMonthlyCostMin())
                .monthlyCostMax(post.getMonthlyCostMax())

                .preferredGender(post.getPreferredGender())
                .preferredMinAge(post.getMinAge())
                .preferredMaxAge(post.getMaxAge())
                .preferedLifeStyle(post.getLifeStyle())
                .preferedPersonality(post.getPersonality())
                .preferredSmoking(post.getIsSmoking())
                .preferredSnoring(post.getIsSnoring())
                .preferredHasPet(post.getHasPet())
                .latitude(post.getRegionLatitude())
                .longitude(post.getRegionLongitude())
                .detailInfo(post.getDetailDescription())
                .additionalInfo(post.getAdditionalDescription())

//                .comments(responses)
//                .applicantCount(Optional.ofNullable(post.getApplyRecords())
//                        .map(records -> records.size() - 1)
//                        .orElse(0))
//                .imgUrl(Optional.ofNullable(post.getImages())
//                        .map(images -> images.stream()
//
//                                .sorted(Comparator.comparing(Images::getDisplayOrder))
//                                .map(Images::getImageUrl)
//                                .collect(Collectors.toList()))
//                        .orElse(new ArrayList<>()))
                .build();
    }

    public static RecruitCoreResponse fromApplyToRecruit(ApplyRecord record) {
        RecruitPost post = record.getPost();
        return RecruitCoreResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .address(post.getAddress())
                .recruitStatus(post.getStatus())
                .rentCostMin(post.getRentCostMin())
                .monthlyMinCost(post.getMonthlyCostMin())
                //.createdDate(post)
                .recruitCount(post.getRecruitCount())
                //.applicantCount() 필요하면 나중에 - 클릭못하도록
                .build();
    }

    public static RecruitCoreResponse fromRecruit(RecruitPost post) {
        return RecruitCoreResponse.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .address(post.getAddress())
                .recruitStatus(post.getStatus())
                .rentCostMin(post.getRentCostMin())
                .monthlyMinCost(post.getMonthlyCostMin())
                .recruitCount(post.getRecruitCount())
                //.applicantCount()
                //.commentCount()
                .build();
    }
}
