package org.example.alarmservice.repository;

import org.example.alarmservice.domain.PushToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PushTokenRepository extends JpaRepository<PushToken, Long> {

    Optional<PushToken> findByToken(String token);

    List<PushToken> findAllByUserIdAndActiveTrue(String userId);

    boolean existsByToken(String token);

}