package com.softkit.service;

import com.softkit.dto.GameTurnStatsDTO;
import com.softkit.dto.NewGameDTO;
import com.softkit.dto.PlayerTurnDTO;
import com.softkit.exception.CustomException;
import com.softkit.model.GameSession;
import com.softkit.model.GameTurn;
import com.softkit.model.User;
import com.softkit.types.GameState;
import com.softkit.types.PlayerType;
import com.softkit.vo.TurnDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.softkit.utils.TurnUtils.*;

@Service
@RequiredArgsConstructor
public class GameService {
    private final UserService userService;
    private final GameTurnService gameTurnService;
    private final GameSessionService gameSessionService;

    public GameTurnStatsDTO startNewGame(String userName, NewGameDTO game) {
        User user = userService.findUser(userName);
        boolean userHasActiveGames = gameSessionService.findLatestUserSession(user.getId())
                .map(GameSession::getGameState)
                .map(gameState -> GameState.IN_PROGRESS == gameState)
                .orElse(false);

        if (userHasActiveGames) {
            throw new CustomException("User has unfinished games!", HttpStatus.CONFLICT);
        }

        String sessionId = gameSessionService.startSession(user.getId()).getSessionId();

        if (PlayerType.HUMAN == game.getStartPlayer()) {
            if (!isValidStartValue(game.getValue())) {
                throw new CustomException("User has provided incorrect start value!", HttpStatus.BAD_REQUEST);
            }

            GameTurn userTurn = gameTurnService.saveTurn(sessionId, PlayerType.HUMAN, game.getValue());
            processTurn(sessionId, PlayerType.AI, generateTurnValue(userTurn.getValue()));
        } else {
            processTurn(sessionId, PlayerType.AI, generateStartValue());
        }

        return buildStatsResponse(sessionId);
    }

    public GameTurnStatsDTO turn(String userName, PlayerTurnDTO turn) {
        Long turnValue = turn.getValue();
        User user = userService.findUser(userName);

        Optional<GameSession> lastSession = gameSessionService.findLatestUserSession(user.getId());
        boolean userHasNoActiveGames = lastSession
                .map(GameSession::getGameState)
                .map(gameState -> GameState.FINISHED == gameState)
                .orElse(true);

        if (userHasNoActiveGames) {
            throw new CustomException("User has no active games!", HttpStatus.CONFLICT);
        }

        String sessionId = lastSession.get().getSessionId();

        Long previousValue = gameTurnService.findLastGameTurn(sessionId).getValue();

        if (!isValidTurn(previousValue, turnValue)) {
            throw new CustomException("User turn is incorrect!", HttpStatus.BAD_REQUEST);
        }

        GameState gameState = processTurn(sessionId, PlayerType.HUMAN, turnValue);

        if (GameState.FINISHED != gameState) {
            processTurn(sessionId, PlayerType.AI, generateTurnValue(turnValue));
        }

        return buildStatsResponse(sessionId);
    }

    private GameState processTurn(String sessionId, PlayerType playerType, Long value) {
        gameTurnService.saveTurn(sessionId, playerType, value);

        if (value == 1) {
            return gameSessionService.finishGame(sessionId).getGameState();
        }

        return GameState.IN_PROGRESS;
    }

    private GameTurnStatsDTO buildStatsResponse(String sessionId) {
        List<GameTurn> allGameTurns = gameTurnService.findAllGameTurns(sessionId);
        return new GameTurnStatsDTO(allGameTurns.stream().map(t -> new TurnDTO(t.getValue(), t.getPlayerType())).collect(Collectors.toList()));
    }
}
