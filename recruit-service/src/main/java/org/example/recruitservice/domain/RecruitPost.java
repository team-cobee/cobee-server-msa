package org.example.recruitservice.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.recruitservice.domain.Enum.Gender;
import org.example.recruitservice.domain.Enum.LifeStyle;
import org.example.recruitservice.domain.Enum.Personality;
import org.example.recruitservice.domain.Enum.RecruitStatus;
import org.example.recruitservice.dto.RecruitRequest;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class RecruitPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long ownerId;  // memberId 넣기

    @Column
    private String title;

    @Column
    private Integer recruitCount;

    @Column
    private Integer rentCostMin;

    @Column
    private Integer rentCostMax;

    @Column
    private Integer monthlyCostMin;

    @Column
    private Integer monthlyCostMax;

    @Column
    private Integer minAge;

    @Column
    private Integer maxAge;

    @Column
    @Enumerated(EnumType.STRING)
    private Gender preferredGender;

    @Column
    @Enumerated(EnumType.STRING)
    private LifeStyle lifeStyle;

    @Column
    @Enumerated(EnumType.STRING)
    private Personality personality;

    @Column
    private Boolean isSmoking;

    @Column
    private Boolean isSnoring;

    @Column
    private Boolean hasPet;

    @Column
    private Boolean hasRoom;

    @Column
    private String address;

    @Column(columnDefinition = "TEXT")
    private String detailDescription;

    @Column(columnDefinition = "TEXT")
    private String additionalDescription;

    @Column
    @Enumerated(EnumType.STRING)
    private RecruitStatus status;

    @Column
    private Double regionLatitude; // 위도

    @Column
    private Double regionLongitude; // 경도

    public void updatePost(RecruitRequest dto) {
        if (dto.getTitle() != null) {
            this.title = dto.getTitle();
        }
        if (dto.getRecruitCount() != null) {
            this.recruitCount = dto.getRecruitCount();
        }
        if (dto.getRentCostMin() != null) {
            this.rentCostMin = dto.getRentCostMin();
        }
        if (dto.getRentCostMax() != null) {
            this.rentCostMax = dto.getRentCostMax();
        }
        if (dto.getMonthlyCostMin() != null) {
            this.monthlyCostMin = dto.getMonthlyCostMin();
        }
        if (dto.getMonthlyCostMax() != null) {
            this.monthlyCostMax = dto.getMonthlyCostMax();
        }
        if (dto.getMinAge() != null) {
            this.minAge = dto.getMinAge();
        }
        if (dto.getMaxAge() != null) {
            this.maxAge = dto.getMaxAge();
        }
        if(dto.getGender() != null) {
            this.preferredGender = dto.getGender();
        }
        if (dto.getLifestyle() != null) {
            this.lifeStyle = dto.getLifestyle();
        }
        if (dto.getPersonality() != null) {
            this.personality = dto.getPersonality();
        }
        if (dto.getIsSmoking() != null) {
            this.isSmoking = dto.getIsSmoking();
        }
        if (dto.getIsSnoring() != null) {
            this.isSnoring = dto.getIsSnoring();
        }
        if (dto.getHasPet() != null) {
            this.hasPet = dto.getHasPet();
        }
        if (dto.getHasRoom() != null) {
            this.hasRoom = dto.getHasRoom();
        }
        if (dto.getAddress() != null) {
            this.address = dto.getAddress();
        }
        if (dto.getDetailDescription() != null) {
            this.detailDescription = dto.getDetailDescription();
        }
        if (dto.getAdditionalDescription() != null) {
            this.additionalDescription = dto.getAdditionalDescription();
        }
    }

//    아래 속성은 추후 조정
//    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
//    private List<Comment> comments;
//
//    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
//    private List<ApplyRecord> applyRecords = new ArrayList<>();
//
//    @OneToMany(mappedBy = "recruitPost", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Images> images = new ArrayList<>();
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private Member member;

}