package com.mika.kiskotaan.services.impl;

import com.mika.kiskotaan.errors.notfound.NotFoundException;
import com.mika.kiskotaan.mappers.player.PlayerMapper;
import com.mika.kiskotaan.models.Player;
import com.mika.kiskotaan.repositories.PlayerRepository;
import com.mika.kiskotaan.services.PlayerService;
import kiskotaan.openapi.model.PlayerResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private final PlayerMapper mapper;
    private final PlayerRepository repository;

    public List<PlayerResource> getPlayers() {
        return repository.findAll().stream()
                .map(mapper::toResource)
                .collect(Collectors.toList());
    }

    public PlayerResource getPlayer(Long id) {
        return repository.findById(id)
                .map(mapper::toResource)
                .orElseThrow(() -> new NotFoundException(new Player(), id));
    }

    public PlayerResource addPlayer(PlayerResource resource) {
        Player newPlayer = repository.save(mapper.toModel(resource));
        return mapper.toResource(newPlayer);
    }

    public void deletePlayer(Long id) {
        repository.deleteById(id);
    }
}
