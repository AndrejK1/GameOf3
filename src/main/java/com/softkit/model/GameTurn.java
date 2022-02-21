package com.softkit.model;

import com.softkit.types.PlayerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;


@Data
@Entity
@Table(name = "game_turn")
@NoArgsConstructor
@AllArgsConstructor
public class GameTurn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "session_id")
    private String gameSessionId;

    private Long value;

    @Column(name = "player_type")
    @Enumerated(EnumType.STRING)
    private PlayerType playerType;

    @Column(name = "turn_timestamp")
    private Date turnTimestamp;
}
