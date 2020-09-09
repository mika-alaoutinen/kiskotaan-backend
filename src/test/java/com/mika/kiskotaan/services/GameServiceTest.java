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
import com.mika.kiskotaan.testdata.TestResources;
import kiskotaan.openapi.model.GameResource;
import kiskotaan.openapi.model.NewGameResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameServiceTest {

    private static final Long GAME_ID = 20L;
    private static final Long SCORE_CARD_ID = 30L;
    private static final ScoreCard SCORE_CARD = TestModels.scoreCard();
    private static final NewGameResource NEW_GAME_RESOURCE = new NewGameResource()
            .scoreCardId(new BigDecimal(30));

    @Mock private GameDao dao;
    @Mock private ScoreCardDao scoreCardDao;
    @Mock private GameMapper mapper;
    @InjectMocks private GameServiceImpl service;

    @Test
    public void shouldGetGame() {
        Game game = new Game();
        when(dao.getGame(GAME_ID)).thenReturn(Optional.of(game));
        when(mapper.toResource(game)).thenReturn(TestResources.gameResource());

        GameResource gameResource = service.getGame(GAME_ID);
        assertNotNull(gameResource);
        verify(dao, times(1)).getGame(GAME_ID);
        verify(mapper, times(1)).toResource(game);
    }

    @Test
    public void shouldHandleGameNotFound() {
        when(dao.getGame(GAME_ID)).thenReturn(Optional.empty());
        NotFoundException e = assertThrows(NotFoundException.class, () ->
                service.getGame(GAME_ID));

        assertEquals("Could not find game with ID 20", e.getMessage());
        verify(dao, times(1)).getGame(GAME_ID);
    }

    @Test
    public void shouldStartGame() {
        final Game savedGame = new Game();
        when(scoreCardDao.getScoreCard(SCORE_CARD_ID)).thenReturn(Optional.of(SCORE_CARD));
        when(dao.addGame(any(Game.class))).thenReturn(savedGame);
        when(mapper.toResource(savedGame)).thenReturn(new GameResource());

        GameResource game = service.startGame(NEW_GAME_RESOURCE);
        assertNotNull(game);
        verify(scoreCardDao, times(1)).getScoreCard(SCORE_CARD_ID);
        verify(dao, times(1)).addGame(any(Game.class));
        verify(mapper, times(1)).toResource(any(Game.class));
    }

    @Test
    public void shouldHandleInvalidScoreCardIdOnStartGame() {
        when(scoreCardDao.getScoreCard(SCORE_CARD_ID)).thenReturn(Optional.empty());

        GameException e = assertThrows(GameException.class, () -> service.startGame(NEW_GAME_RESOURCE));
        assertEquals("Could not start new game with given score card ID 30", e.getMessage());
        verify(scoreCardDao, times(1)).getScoreCard(SCORE_CARD_ID);
        verify(dao, never()).addGame(any(Game.class));
        verify(mapper, never()).toResource(any(Game.class));
    }

    @Test
    public void shouldEndGame() {
        Game game = new Game();
        game.setId(GAME_ID);
        when(dao.getGame(GAME_ID)).thenReturn(Optional.of(game));

        service.endGame(GAME_ID);
        verify(dao, times(1)).getGame(GAME_ID);
        verify(dao, times(1)).deleteGame(GAME_ID);
    }

    @Test
    public void shouldHandleInvalidGameIdOnEndGame() {
        when(dao.getGame(GAME_ID)).thenReturn(Optional.empty());

        service.endGame(GAME_ID);
        verify(dao, times(1)).getGame(GAME_ID);
        verify(dao, never()).deleteGame(anyLong());
    }
}
