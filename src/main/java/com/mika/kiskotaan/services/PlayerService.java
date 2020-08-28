package com.mika.kiskotaan.services;

import kiskotaan.openapi.model.PlayerResource;

import java.util.List;

public interface PlayerService {
    List<PlayerResource> getPlayers();
    PlayerResource addPlayer();
    PlayerResource getPlayer(String id);
    void deletePlayer(String id);
}
