package com.mika.kiskotaan.services.impl;

import com.mika.kiskotaan.dao.PlayerDao;
import com.mika.kiskotaan.errors.badrequest.PlayerException;
import com.mika.kiskotaan.errors.notfound.NotFoundException;
import com.mika.kiskotaan.mappers.PlayerMapper;
import com.mika.kiskotaan.models.Player;
import com.mika.kiskotaan.services.PlayerService;
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
    private final PlayerDao dao;
    private final PlayerMapper mapper;

    @Override
    public List<PlayerResource> getPlayers() {
        return dao.getPlayers().stream()
                .map(mapper::toResource)
                .collect(Collectors.toList());
    }

    @Override
    public PlayerResource getPlayer(Long id) throws NotFoundException {
        return dao.getPlayer(id)
                .map(mapper::toResource)
                .orElseThrow(() -> new NotFoundException(new Player(), id));
    }

    @Override
    public PlayerResource addPlayer(NewPlayerResource resource) throws PlayerException {
        if (dao.existsByName(resource.getName())) {
            throw new PlayerException(resource);
        }

        return Stream.of(mapper.toModel(resource))
                .map(dao::addPlayer)
                .map(mapper::toResource)
                .findAny()
                .orElseThrow(() -> new PlayerException(resource));
    }

    @Override
    public void deletePlayer(Long id) {
        dao.deletePlayer(id);
    }
}
