package com.mika.kiskotaan.dao.impl;

import com.mika.kiskotaan.dao.PlayerDao;
import com.mika.kiskotaan.models.Player;
import com.mika.kiskotaan.repositories.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlayerDaoImpl implements PlayerDao {
    private final PlayerRepository repository;

    @Override
    public List<Player> getPlayers() {
        return repository.findAll();
    }

    @Override
    public List<Player> getPlayersByIds(Collection<Long> playerIds) {
        return repository.findAllById(playerIds);
    }

    @Override
    public Optional<Player> getPlayer(Long id) {
        return repository.findById(id);
    }

    @Override
    public Player addPlayer(Player newPlayer) {
        return repository.save(newPlayer);
    }

    @Override
    public void deletePlayer(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsByIds(Collection<Long> playerIds) {
        return playerIds.stream().allMatch(repository::existsById);
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByNameIgnoreCase(name);
    }
}
