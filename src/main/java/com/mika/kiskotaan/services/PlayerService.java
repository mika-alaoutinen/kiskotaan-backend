package com.mika.kiskotaan.services;

import com.mika.kiskotaan.errors.badrequest.PlayerException;
import com.mika.kiskotaan.errors.notfound.NotFoundException;
import com.mika.kiskotaan.models.Player;
import kiskotaan.openapi.model.NewPlayerResource;
import kiskotaan.openapi.model.PlayerResource;

import java.util.Collection;
import java.util.List;

public interface PlayerService {
    List<PlayerResource> getPlayers();
    PlayerResource getPlayer(Long id) throws NotFoundException;
    PlayerResource addPlayer(NewPlayerResource resource) throws PlayerException;
    void deletePlayer(Long id);

    List<Player> getPlayers(Collection<Long> playerIds);
    boolean existsByIds(Collection<Long> playerIds);
}
