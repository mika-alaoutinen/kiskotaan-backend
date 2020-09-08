package com.mika.kiskotaan.dao;

import com.mika.kiskotaan.models.Game;

public interface GameDao {
    Game addGame(Game game);
    void deleteGame(Long id);
}
