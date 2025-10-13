package org.example.recruitservice.repository;

import org.example.recruitservice.domain.ApplyRecord;
import org.example.recruitservice.domain.Enum.MatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplyRepository extends JpaRepository<ApplyRecord, Long> {
    @Query("""
        select a
        from ApplyRecord a
        where a.post.id = :postId
          and a.appliedMemberId <> :memberId
        """)
    List<ApplyRecord> findMyPostAppliersExceptMe(@Param("postId") Long postId,
                                                 @Param("memberId") Long memberId);

    @Query("""
        select a
        from ApplyRecord a
        where a.post.id = :postId
          and a.appliedMemberId <> :memberId
                  and a.matchStatus = :status
        """)
    List<ApplyRecord> findMyPostAppliersExceptMeWithStatus(@Param("postId") Long postId,
                                                           @Param("memberId") Long memberId,
                                                           @Param("status") MatchStatus status);

    List<ApplyRecord> findApplyRecordsByAppliedMemberIdAndMatchStatus(@Param("memberId") Long memberId, @Param("status") MatchStatus status);

}
