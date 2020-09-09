package com.mika.kiskotaan.services;

import com.mika.kiskotaan.errors.badrequest.GameException;
import com.mika.kiskotaan.errors.notfound.NotFoundException;
import kiskotaan.openapi.model.GameResource;
import kiskotaan.openapi.model.NewGameResource;

public interface GameService {
    GameResource getGame(Long id) throws NotFoundException;
    GameResource startGame(NewGameResource newGameResource) throws GameException;
    void endGame(Long id);
}
