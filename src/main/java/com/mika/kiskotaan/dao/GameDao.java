package com.mika.kiskotaan.dao;

import com.mika.kiskotaan.models.Game;

import java.util.Optional;

public interface GameDao {
    Optional<Game> getGame(Long id);
    Game addGame(Game game);
    void deleteGame(Long id);
}
