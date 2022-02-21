package com.softkit.repository;

import com.softkit.model.GameSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameSessionRepository extends JpaRepository<GameSession, String> {
    Optional<GameSession> findFirstByUserIdOrderByGameStartedTimestampDesc(Long userId);
}
