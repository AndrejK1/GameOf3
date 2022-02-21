package com.softkit.vo;

import com.softkit.types.PlayerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TurnDTO {
    private Long value;
    private PlayerType player;
}
