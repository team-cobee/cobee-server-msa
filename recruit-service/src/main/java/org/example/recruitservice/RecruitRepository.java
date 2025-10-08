package org.example.recruitservice;

import org.example.recruitservice.domain.RecruitPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitRepository extends JpaRepository<RecruitPost, Long> {
}
