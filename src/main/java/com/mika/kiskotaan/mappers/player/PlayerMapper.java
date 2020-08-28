package com.mika.kiskotaan.mappers.player;

import com.mika.kiskotaan.models.Player;
import kiskotaan.openapi.model.PlayerResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    Player toModel(PlayerResource resource);
    PlayerResource toResource(Player model);
}
