package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.ScoreCard;
import kiskotaan.openapi.model.PlayerResource;
import kiskotaan.openapi.model.ScoreCardResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class ScoreCardMapperDecorator implements ScoreCardMapper {

    @Autowired
    @Qualifier("delegate")
    private ScoreCardMapper mapper;

    @Override
    public ScoreCardResource toResource(ScoreCard model) {
        ScoreCardResource resource = mapper.toResource(model);
        resource.setPlayers(sortByName(resource.getPlayers()));
        return resource;
    }

    private Set<PlayerResource> sortByName(Set<PlayerResource> players) {
        return players.stream()
                .sorted(Comparator.comparing(PlayerResource::getName))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
