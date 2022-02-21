package com.softkit.model;

import com.softkit.types.GameState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@Table(name = "game_session")
@NoArgsConstructor
@AllArgsConstructor
public class GameSession {
    @Id
    @Column(name = "id")
    private String sessionId;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "game_state")
    @Enumerated(EnumType.STRING)
    private GameState gameState;

    @Column(name = "game_started_timestamp")
    private Date gameStartedTimestamp;
}
