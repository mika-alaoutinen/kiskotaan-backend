package com.mika.kiskotaan.services;

import com.mika.kiskotaan.errors.badrequest.GameException;
import com.mika.kiskotaan.errors.notfound.NotFoundException;
import kiskotaan.openapi.model.GameResource;

public interface GameService {
    GameResource startGame(Long scoreCardId) throws GameException;
    GameResource endGame(Long gameId, Long scoreCardId) throws NotFoundException;
}
