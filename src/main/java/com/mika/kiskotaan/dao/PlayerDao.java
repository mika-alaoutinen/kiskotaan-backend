package com.mika.kiskotaan.dao;

import com.mika.kiskotaan.models.Player;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface PlayerDao {
    List<Player> getPlayers();
    List<Player> getPlayersByIds(Collection<Long> playerIds);
    Optional<Player> getPlayer(Long id);
    Player addPlayer(Player newPlayer);
    void deletePlayer(Long id);
    boolean existsByIds(Collection<Long> playerIds);
    boolean existsByName(String name);
}
