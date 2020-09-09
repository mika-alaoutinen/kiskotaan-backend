package com.mika.kiskotaan.services;

import com.mika.kiskotaan.errors.badrequest.GameException;
import com.mika.kiskotaan.errors.notfound.NotFoundException;
import com.mika.kiskotaan.models.Game;
import kiskotaan.openapi.model.GameResource;
import kiskotaan.openapi.model.NewGameResource;

public interface GameService {
    Game getGame(Long id) throws NotFoundException;
    GameResource startGame(NewGameResource newGameResource) throws GameException;
    GameResource endGame(Long id) throws NotFoundException;
}
