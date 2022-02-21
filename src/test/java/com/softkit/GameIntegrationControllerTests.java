package com.softkit;

import com.softkit.dto.GameTurnStatsDTO;
import com.softkit.dto.NewGameDTO;
import com.softkit.dto.PlayerTurnDTO;
import com.softkit.types.GameState;
import com.softkit.types.PlayerType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.softkit.utils.UserUtils.getValidUserForSignup;

class GameIntegrationControllerTests extends AbstractControllerTest {
    private static final String SIGNUP_URL = "/users/signup";
    private static final String START_GAME_URL = "/game/start";
    private static final String TURN_URL = "/game/turn";

    private String simpleSignup() {
        return this.restTemplate.postForObject(getBaseUrl() + SIGNUP_URL, getValidUserForSignup(), String.class);
    }

    @Test
    void tryToTurnWithoutStartGame() {
        String token = simpleSignup();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        ResponseEntity responseEntity = this.restTemplate.postForEntity(getBaseUrl() + TURN_URL,
                new HttpEntity<>(new PlayerTurnDTO(34L), headers),
                GameTurnStatsDTO.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());

    }

    @Test
    void startGameTwice() {
        String token = simpleSignup();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        ResponseEntity responseEntity = this.restTemplate.postForEntity(getBaseUrl() + START_GAME_URL,
                new HttpEntity<>(new NewGameDTO(), headers),
                GameTurnStatsDTO.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        ResponseEntity responseEntity2 = this.restTemplate.postForEntity(getBaseUrl() + START_GAME_URL,
                new HttpEntity<>(new NewGameDTO(PlayerType.HUMAN, 34L), headers),
                GameTurnStatsDTO.class);
        Assertions.assertEquals(HttpStatus.CONFLICT, responseEntity2.getStatusCode());
    }

    @Test
    void normalGame() {
        String token = simpleSignup();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        ResponseEntity<GameTurnStatsDTO> responseEntity = this.restTemplate.postForEntity(getBaseUrl() + START_GAME_URL,
                new HttpEntity<>(new NewGameDTO(PlayerType.HUMAN, 34L), headers),
                GameTurnStatsDTO.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(11L, responseEntity.getBody().getValue());
        Assertions.assertEquals(GameState.IN_PROGRESS, responseEntity.getBody().getState());

        ResponseEntity<GameTurnStatsDTO> responseEntityTurn2 = this.restTemplate.postForEntity(getBaseUrl() + TURN_URL,
                new HttpEntity<>(new PlayerTurnDTO(4L), headers),
                GameTurnStatsDTO.class);

        Assertions.assertEquals(HttpStatus.OK, responseEntityTurn2.getStatusCode());
        Assertions.assertEquals(1, responseEntityTurn2.getBody().getValue());
        Assertions.assertEquals(GameState.FINISHED, responseEntityTurn2.getBody().getState());
    }

    @Test
    void userError() {
        String token = simpleSignup();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        ResponseEntity<GameTurnStatsDTO> responseEntity = this.restTemplate.postForEntity(getBaseUrl() + START_GAME_URL,
                new HttpEntity<>(new NewGameDTO(PlayerType.HUMAN, 34L), headers),
                GameTurnStatsDTO.class);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(11L, responseEntity.getBody().getValue());
        Assertions.assertEquals(GameState.IN_PROGRESS, responseEntity.getBody().getState());

        ResponseEntity<GameTurnStatsDTO> responseEntityTurn2 = this.restTemplate.postForEntity(getBaseUrl() + TURN_URL,
                new HttpEntity<>(new PlayerTurnDTO(44L), headers),
                GameTurnStatsDTO.class);

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, responseEntityTurn2.getStatusCode());
    }
}
