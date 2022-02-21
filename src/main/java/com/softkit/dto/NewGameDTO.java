package com.softkit.dto;

import com.softkit.types.PlayerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class NewGameDTO {
    @NotNull
    private PlayerType startPlayer;
    private Long value;

    public NewGameDTO() {
        startPlayer = PlayerType.AI;
    }
}
