package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.ScoreCard;
import kiskotaan.openapi.model.NewScoreCardResource;
import kiskotaan.openapi.model.ScoreCardResource;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = { CourseMapper.class, HoleMapper.class, PlayerMapper.class, ScoreRowMapper.class, ScoreMapper.class }
)
@DecoratedWith(ScoreCardMapperDecorator.class)
public interface ScoreCardMapper {
    ScoreCard toModel(ScoreCardResource resource);
    ScoreCard toModel(NewScoreCardResource resource);
    ScoreCardResource toResource(ScoreCard model);
}