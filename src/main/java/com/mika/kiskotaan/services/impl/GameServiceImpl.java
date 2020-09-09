package com.mika.kiskotaan.services.impl;

import com.mika.kiskotaan.dao.GameDao;
import com.mika.kiskotaan.dao.ScoreCardDao;
import com.mika.kiskotaan.errors.badrequest.GameException;
import com.mika.kiskotaan.errors.notfound.NotFoundException;
import com.mika.kiskotaan.mappers.GameMapper;
import com.mika.kiskotaan.models.Game;
import com.mika.kiskotaan.models.ScoreCard;
import com.mika.kiskotaan.services.GameService;
import kiskotaan.openapi.model.GameResource;
import kiskotaan.openapi.model.NewGameResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameDao dao;
    private final ScoreCardDao scoreCardDao;
    private final GameMapper mapper;

    @Override
    public GameResource startGame(NewGameResource newGameResource) throws GameException {
        Long scoreCardId = newGameResource.getScoreCardId().longValue();
        ScoreCard scoreCard = scoreCardDao.getScoreCard(scoreCardId)
                .orElseThrow(() -> new GameException(scoreCardId));

        return Stream.of(scoreCard)
                .map(card -> new Game(false, false, 1, card))
                .map(dao::addGame)
                .map(mapper::toResource)
                .findAny()
                .orElseThrow();
    }

    @Override
    public GameResource endGame(Long id) throws NotFoundException {
        Game game = getGame(id);
        game.setGameOver(true);
        dao.deleteGame(id);
        return mapper.toResource(game);
    }

    @Override
    public Game getGame(Long id) throws NotFoundException {
        return dao.getGame(id)
                .orElseThrow(() -> new NotFoundException(new Game(), id));
    }
}
