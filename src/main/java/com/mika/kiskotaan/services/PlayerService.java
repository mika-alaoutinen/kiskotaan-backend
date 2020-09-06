package com.mika.kiskotaan.services;

import com.mika.kiskotaan.errors.badrequest.PlayerException;
import com.mika.kiskotaan.errors.notfound.NotFoundException;
import kiskotaan.openapi.model.NewPlayerResource;
import kiskotaan.openapi.model.PlayerResource;

import java.util.List;

public interface PlayerService {
    List<PlayerResource> getPlayers();
    PlayerResource getPlayer(Long id) throws NotFoundException;
    PlayerResource addPlayer(NewPlayerResource resource) throws PlayerException;
    void deletePlayer(Long id);
}
