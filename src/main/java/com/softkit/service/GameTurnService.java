package com.softkit.service;

import com.softkit.model.GameTurn;
import com.softkit.repository.GameTurnRepository;
import com.softkit.types.PlayerType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameTurnService {
    private final GameTurnRepository gameTurnRepository;

    public GameTurn saveTurn(String sessionId, PlayerType playerType, Long value) {
        return gameTurnRepository.save(new GameTurn(
                null,
                sessionId,
                value,
                playerType,
                new Date()
        ));
    }

    public GameTurn findLastGameTurn(String sessionId) {
        return gameTurnRepository.findFirstByGameSessionIdOrderByTurnTimestampDesc(sessionId);
    }

    public List<GameTurn> findAllGameTurns(String sessionId) {
        return gameTurnRepository.findAllByGameSessionIdOrderByTurnTimestampDesc(sessionId);
    }
}
