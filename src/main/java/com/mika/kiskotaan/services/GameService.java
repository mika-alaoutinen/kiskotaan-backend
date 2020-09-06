package com.mika.kiskotaan.services;

import com.mika.kiskotaan.errors.notfound.NotFoundException;
import kiskotaan.openapi.model.GameResource;

public interface GameService {
    GameResource startGame(Long scoreCardId);
    GameResource endGame(Long scoreCardId) throws NotFoundException;
}
