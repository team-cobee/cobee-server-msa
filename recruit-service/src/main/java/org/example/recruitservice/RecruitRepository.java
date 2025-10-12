package org.example.recruitservice;

import org.example.recruitservice.domain.RecruitPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruitRepository extends JpaRepository<RecruitPost, Long> {
}
