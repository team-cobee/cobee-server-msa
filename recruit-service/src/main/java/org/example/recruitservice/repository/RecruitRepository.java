package org.example.recruitservice.repository;

import org.example.recruitservice.domain.RecruitPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RecruitRepository extends JpaRepository<RecruitPost, Long> {
    List<RecruitPost> findAllByOwnerId(Long id);

    List<RecruitPost> findAllByUpdatedAtAfter(LocalDateTime updatedAt);

    @Query("SELECT rp FROM RecruitPost rp")
    List<RecruitPost> findAllForSync();
}
