package com.mika.kiskotaan.services.impl;

import com.mika.kiskotaan.mappers.player.PlayerMapper;
import com.mika.kiskotaan.models.Player;
import com.mika.kiskotaan.repositories.PlayerRepository;
import com.mika.kiskotaan.services.PlayerService;
import kiskotaan.openapi.model.PlayerResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private final PlayerMapper mapper;
    private final PlayerRepository repository;

    public List<PlayerResource> getPlayers() {
        return null;
    }

    public PlayerResource addPlayer() {
        return null;
    }

    public PlayerResource getPlayer(String id) {
        return null;
    }

    public void deletePlayer(String id) {

    }
}
