package com.mika.kiskotaan.dao.impl;

import com.mika.kiskotaan.dao.GameDao;
import com.mika.kiskotaan.models.Game;
import com.mika.kiskotaan.repositories.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GameDaoImpl implements GameDao {
    private final GameRepository repository;

    @Override
    public Game startGame(Long scoreCardId) {
        return null;
    }

    @Override
    public Game endGame(Long screCardId) {
        return null;
    }
}
