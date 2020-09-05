package com.mika.kiskotaan.services.impl;

import com.mika.kiskotaan.errors.badrequest.PlayerException;
import com.mika.kiskotaan.errors.notfound.NotFoundException;
import com.mika.kiskotaan.mappers.PlayerMapper;
import com.mika.kiskotaan.models.Player;
import com.mika.kiskotaan.repositories.PlayerRepository;
import com.mika.kiskotaan.services.PlayerService;
import kiskotaan.openapi.model.NewPlayerResource;
import kiskotaan.openapi.model.PlayerResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private final PlayerMapper mapper;
    private final PlayerRepository repository;

    @Override
    public List<PlayerResource> getPlayers() {
        return repository.findAll().stream()
                .map(mapper::toResource)
                .collect(Collectors.toList());
    }

    @Override
    public PlayerResource getPlayer(Long id) throws NotFoundException {
        return repository.findById(id)
                .map(mapper::toResource)
                .orElseThrow(() -> new NotFoundException(new Player(), id));
    }

    @Override
    public PlayerResource addPlayer(NewPlayerResource resource) throws PlayerException {
        if (repository.existsByNameIgnoreCase(resource.getName())) {
            throw new PlayerException(resource);
        }

        return Stream.of(mapper.toModel(resource))
                .map(repository::save)
                .map(mapper::toResource)
                .findAny()
                .orElseThrow(() -> new PlayerException(resource));
    }

    @Override
    public void deletePlayer(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Player> getPlayers(Collection<Long> playerIds) {
        return repository.findAllById(playerIds);
    }

    @Override
    public boolean existsByIds(Collection<Long> playerIds) {
        return playerIds.stream()
                .allMatch(repository::existsById);
    }
}
