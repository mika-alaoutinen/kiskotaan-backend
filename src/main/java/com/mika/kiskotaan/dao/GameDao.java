package com.mika.kiskotaan.dao;

import com.mika.kiskotaan.models.Game;

public interface GameDao {
    Game startGame(Long scoreCardId);
    Game endGame(Long screCardId);
}
