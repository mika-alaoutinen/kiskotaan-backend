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
    public Game addGame(Game game) {
        return repository.save(game);
    }

    @Override
    public void deleteGame(Long id) {
        repository.deleteById(id);
    }
}
