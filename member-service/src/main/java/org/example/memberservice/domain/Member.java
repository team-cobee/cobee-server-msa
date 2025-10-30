package org.example.memberservice.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.common.BaseEntity;

@Getter
@NoArgsConstructor
@Entity
public class Member extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(nullable = true)
    private String email;

    @Column
    private String birthDate;

    @Column
    private String gender;

    @Column(nullable = false)
    private String socialId;

    // 회원가입 완료 여부 (소셜 로그인 후 추가 정보 입력 완료 여부)
    @Column(nullable = false)
    private Boolean isCompleted;

    @Column
    private String profileUrl;

    @Column
    private Boolean ocrValidation;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private PublicProfile publicProfile;

    @OneToOne(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private MemberPreference memberPreference;

    @Builder
    public Member(String name, String email, String birthDate, String gender, String socialId, String profileUrl,Boolean isCompleted, Boolean ocrValidation) {
        this.name = name;
        this.email = email;
        this.birthDate = birthDate;
        this.gender = gender;
        this.socialId = socialId;
        this.profileUrl = profileUrl;
        this.isCompleted = isCompleted;
        this.ocrValidation = ocrValidation;
    }
    public Member update(String name, String profileUrl) {
        this.name = name;
        this.profileUrl = profileUrl;
        return this;
    }
    public void updateGender(String gender) {
        this.gender = gender;
    }

    public void updateBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
    public void updateOcrValidation(boolean ocrValidation) {
        this.ocrValidation = ocrValidation;
    }

    public void setPublicProfile(PublicProfile publicProfile) {
        this.publicProfile = publicProfile;
    }

    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }
}
