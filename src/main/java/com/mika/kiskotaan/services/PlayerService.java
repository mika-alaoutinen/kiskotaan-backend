package com.mika.kiskotaan.services;

import kiskotaan.openapi.model.PlayerResource;

import java.util.List;

public interface PlayerService {
    List<PlayerResource> getPlayers();
    PlayerResource getPlayer(Long id);
    PlayerResource addPlayer(PlayerResource resource);
    void deletePlayer(Long id);
}
