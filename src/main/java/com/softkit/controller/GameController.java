package com.softkit.controller;

import com.softkit.dto.GameTurnStatsDTO;
import com.softkit.dto.NewGameDTO;
import com.softkit.dto.PlayerTurnDTO;
import com.softkit.security.ProjectUserDetailsService;
import com.softkit.service.GameService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
@Api(tags = "game")
public class GameController {
    private final GameService gameService;
    private final ProjectUserDetailsService userDetailsService;

    @PostMapping("/start")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 409, message = "User has unfinished games")
    })
    public GameTurnStatsDTO startNewGame(@RequestBody @Valid NewGameDTO game) {
        return gameService.startNewGame(userDetailsService.getCurrentAuth(), game);
    }

    @PostMapping("/turn")
    @ApiResponses(value = {
            @ApiResponse(code = 400, message = "Something went wrong"),
            @ApiResponse(code = 409, message = "User don't have unfinished games")
    })
    public GameTurnStatsDTO turn(@RequestBody @Valid PlayerTurnDTO turn) {
        return gameService.turn(userDetailsService.getCurrentAuth(), turn);
    }
}
