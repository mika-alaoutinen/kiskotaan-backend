package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.Game;
import kiskotaan.openapi.model.GameResource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GameMapper {

    @Mapping(source = "resource.scoreCardId", target = "scoreCard.id", ignore = true)
    Game toModel(GameResource resource);

    @Mapping(source = "model.scoreCard.id", target = "id", ignore = true)
    GameResource toResource(Game model);
}
