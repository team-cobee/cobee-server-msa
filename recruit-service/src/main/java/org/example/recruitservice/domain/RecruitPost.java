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
    @Enumerated(EnumType.STRING)
    private Gender preferredGender;

    @Column
    private Integer minAge;

    @Column
    private Integer maxAge;

    @Column
    @Enumerated(EnumType.STRING)
    private LifeStyle lifeStyle;

    @Column
    @Enumerated(EnumType.STRING)
    private Personality personality;

    @Column
    @Enumerated(EnumType.STRING)
    private Boolean isSmoking;

    @Column
    @Enumerated(EnumType.STRING)
    private Boolean isSnoring;

    @Column
    @Enumerated(EnumType.STRING)
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