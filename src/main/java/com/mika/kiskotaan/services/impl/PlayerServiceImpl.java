package com.mika.kiskotaan.services.impl;

import com.mika.kiskotaan.errors.badrequest.PlayerException;
import com.mika.kiskotaan.errors.notfound.NotFoundException;
import com.mika.kiskotaan.mappers.PlayerMapper;
import com.mika.kiskotaan.models.Player;
import com.mika.kiskotaan.repositories.PlayerRepository;
import com.mika.kiskotaan.services.PlayerService;
import com.mika.kiskotaan.validators.PlayerResourceValidator;
import kiskotaan.openapi.model.NewPlayerResource;
import kiskotaan.openapi.model.PlayerResource;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {
    private final PlayerMapper mapper;
    private final PlayerRepository repository;
    private final PlayerResourceValidator validator;

    @Override
    public List<PlayerResource> getPlayers() {
        return repository.findAll().stream()
                .map(mapper::toResource)
                .collect(Collectors.toList());
    }

    @Override
    public PlayerResource getPlayer(Long id) {
        return repository.findById(id)
                .map(mapper::toResource)
                .orElseThrow(() -> new NotFoundException(new Player(), id));
    }

    @Override
    public PlayerResource addPlayer(NewPlayerResource resource) {
        return Stream.of(validator.validateNameIsUnique(resource))
                .map(mapper::toModel)
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
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByNameIgnoreCase(name);
    }
}
