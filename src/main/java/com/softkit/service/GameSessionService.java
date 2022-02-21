package com.softkit.service;

import com.softkit.model.GameSession;
import com.softkit.model.GameTurn;
import com.softkit.repository.GameSessionRepository;
import com.softkit.types.GameState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameSessionService {
    private final GameSessionRepository gameTurnRepository;

    public GameSession finishGame(String sessionId) {
        GameSession session = gameTurnRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("No such session found!"));
        session.setGameState(GameState.FINISHED);
        return gameTurnRepository.save(session);
    }

    public GameSession startSession(Long userId) {
        return gameTurnRepository.save(new GameSession(
                UUID.randomUUID().toString(),
                userId,
                GameState.IN_PROGRESS,
                new Date()
        ));
    }

    public Optional<GameSession> findLatestUserSession(Long userId) {
        return gameTurnRepository.findFirstByUserIdOrderByGameStartedTimestampDesc(userId);
    }
}
