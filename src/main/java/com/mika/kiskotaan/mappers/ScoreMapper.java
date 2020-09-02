package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.Score;
import kiskotaan.openapi.model.ScoreResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScoreMapper {
    Score toModel(ScoreResource resource);
    ScoreResource toResource(Score score);
}
