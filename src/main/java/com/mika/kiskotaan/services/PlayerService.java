package com.mika.kiskotaan.services;

import kiskotaan.openapi.model.PlayerResource;

import java.util.List;

public interface PlayerService {
    List<PlayerResource> getPlayers();
    PlayerResource getPlayer(String id);
    PlayerResource addPlayer();
    void deletePlayer(String id);
}
