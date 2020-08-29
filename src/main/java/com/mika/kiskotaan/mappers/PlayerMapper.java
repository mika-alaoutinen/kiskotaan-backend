package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.Player;
import kiskotaan.openapi.model.NewPlayerResource;
import kiskotaan.openapi.model.PlayerResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PlayerMapper {
    Player toModel(PlayerResource resource);
    Player toModel(NewPlayerResource resource);
    PlayerResource toResource(Player model);
}
