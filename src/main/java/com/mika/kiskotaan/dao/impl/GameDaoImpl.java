package com.mika.kiskotaan.dao.impl;

import com.mika.kiskotaan.dao.GameDao;
import com.mika.kiskotaan.models.Game;
import com.mika.kiskotaan.repositories.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GameDaoImpl implements GameDao {
    private final GameRepository repository;

    @Override
    public Optional<Game> getGame(Long id) {
        return repository.findById(id);
    }

    @Override
    public Game addGame(Game game) {
        return repository.save(game);
    }

    @Override
    public void deleteGame(Long id) {
        repository.deleteById(id);
    }
}
