package com.softkit.dto;

import com.softkit.types.GameState;
import com.softkit.vo.TurnDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameTurnStatsDTO {
    private Long value;
    private GameState state;
    private List<TurnDTO> gameTurns;

    public GameTurnStatsDTO(List<TurnDTO> gameTurns) {
        if (gameTurns != null && !gameTurns.isEmpty()) {
            TurnDTO lastTurn = gameTurns.get(0);
            value = lastTurn.getValue();
            state = value == 1 ? GameState.FINISHED : GameState.IN_PROGRESS;
        }

        this.gameTurns = gameTurns;
    }
}
