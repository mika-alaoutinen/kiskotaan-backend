package com.mika.kiskotaan.services;

import com.mika.kiskotaan.dao.GameDao;
import com.mika.kiskotaan.dao.ScoreCardDao;
import com.mika.kiskotaan.errors.badrequest.GameException;
import com.mika.kiskotaan.errors.notfound.NotFoundException;
import com.mika.kiskotaan.mappers.GameMapper;
import com.mika.kiskotaan.models.Game;
import com.mika.kiskotaan.models.ScoreCard;
import com.mika.kiskotaan.services.impl.GameServiceImpl;
import com.mika.kiskotaan.testdata.TestModels;
import kiskotaan.openapi.model.GameResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    private static final Long GAME_ID = 20L;
    private static final Long SCORE_CARD_ID = 30L;
    private static final ScoreCard SCORE_CARD = TestModels.scoreCard();

    @Mock private GameDao dao;
    @Mock private ScoreCardDao scoreCardDao;
    @Mock private GameMapper mapper;
    @InjectMocks private GameServiceImpl service;

    @Test
    public void shouldStartGame() {
        Game savedGame = new Game();
        when(scoreCardDao.getScoreCard(SCORE_CARD_ID)).thenReturn(Optional.of(SCORE_CARD));
        when(dao.addGame(any(Game.class))).thenReturn(savedGame);
        when(mapper.toResource(savedGame)).thenReturn(new GameResource());

        GameResource game = service.startGame(SCORE_CARD_ID);
        verifySuccessCase(game);
        verify(dao, times(1)).addGame(any(Game.class));
    }

    @Test
    public void shouldHandleInvalidScoreCardIdOnStartGame() {
        when(scoreCardDao.getScoreCard(SCORE_CARD_ID)).thenReturn(Optional.empty());

        GameException e = assertThrows(GameException.class, () -> service.startGame(SCORE_CARD_ID));
        verifyFailureCase("Could not start new game with given score card ID 30", e);
        verify(dao, never()).addGame(any(Game.class));
    }

    @Test
    public void shouldEndGame() {
        when(scoreCardDao.getScoreCard(SCORE_CARD_ID)).thenReturn(Optional.of(SCORE_CARD));
        when(mapper.toResource(any(Game.class))).thenReturn(new GameResource());

        GameResource ended = service.endGame(GAME_ID, SCORE_CARD_ID);
        verifySuccessCase(ended);
        verify(dao, times(1)).deleteGame(GAME_ID);
    }

    @Test
    public void shouldHandleInvalidScoreCardIdOnEndGame() {
        when(scoreCardDao.getScoreCard(SCORE_CARD_ID)).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () ->
                service.endGame(GAME_ID, SCORE_CARD_ID));

        verifyFailureCase("Could not find score card with ID 30", e);
        verify(dao, never()).deleteGame(anyLong());
    }

    private void verifySuccessCase(GameResource game) {
        assertNotNull(game);
        verify(scoreCardDao, times(1)).getScoreCard(SCORE_CARD_ID);
        verify(mapper, times(1)).toResource(any(Game.class));
    }

    private void verifyFailureCase(String errorMessage, Exception e) {
        assertEquals(errorMessage, e.getMessage());
        verify(scoreCardDao, times(1)).getScoreCard(SCORE_CARD_ID);
        verify(mapper, never()).toResource(any(Game.class));
    }
}
