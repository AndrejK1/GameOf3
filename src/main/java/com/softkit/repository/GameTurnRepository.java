package com.softkit.repository;

import com.softkit.model.GameTurn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameTurnRepository extends JpaRepository<GameTurn, Long> {
    List<GameTurn> findAllByGameSessionIdOrderByTurnTimestampDesc(String gameSession);

    GameTurn findFirstByGameSessionIdOrderByTurnTimestampDesc(String gameSession);
}
