package com.mika.kiskotaan.controllers;

import com.mika.kiskotaan.models.Game;
import com.mika.kiskotaan.repositories.GameRepository;
import com.mika.kiskotaan.repositories.ScoreCardRepository;
import com.mika.kiskotaan.testdata.TestModels;
import com.mika.kiskotaan.testdata.TestResources;
import kiskotaan.openapi.model.GameResource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GameControllerTest extends ControllerTest {
    private static final String URL = "/games";
    private static final Long GAME_ID = 11L;
    private static final Long SCORE_CARD_ID = 22L;

    @MockBean private GameRepository repository;
    @MockBean private ScoreCardRepository scoreCardRepository;

    @Test
    public void shouldGetGame() throws Exception {
        when(repository.findById(GAME_ID)).thenReturn(Optional.of(TestModels.game()));

        MvcResult result = performGet(URL + "/" + GAME_ID);
        GameResource game = parseGame(result);
        assertNewGame(game);
    }

    @Test
    public void shouldStartGame() throws Exception {
        when(scoreCardRepository.findById(SCORE_CARD_ID)).thenReturn(Optional.of(TestModels.scoreCard()));
        when(repository.save(any(Game.class))).thenReturn(TestModels.game());

        MvcResult result = performPost(URL, TestResources.newGameResource());
        GameResource game = parseGame(result);
        assertNewGame(game);
    }

    @Test
    public void shouldEndGame() throws Exception {
        doNothing().when(repository).deleteById(GAME_ID);
        performDelete(URL + "/" + GAME_ID);
        verify(repository, times(1)).deleteById(GAME_ID);
    }

    private void assertNewGame(GameResource game) {
        assertFalse(game.isGameOver());
        assertFalse(game.isScoreChanged());
        assertEquals(1, game.getHole());
        assertEquals(new BigDecimal(22), game.getScoreCardId());
    }

    private GameResource parseGame(MvcResult result) throws Exception {
        return mapper.readValue(testUtils.parseResponseString(result), GameResource.class);
    }
}
