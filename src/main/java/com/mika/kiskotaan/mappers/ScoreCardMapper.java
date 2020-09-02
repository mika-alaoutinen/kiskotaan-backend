package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.ScoreCard;
import kiskotaan.openapi.model.NewScoreCardResource;
import kiskotaan.openapi.model.ScoreCardResource;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        uses = { CourseMapper.class, HoleMapper.class, PlayerMapper.class, ScoreMapper.class, ScoreRowMapper.class }
)
@DecoratedWith(ScoreCardMapperDecorator.class)
public interface ScoreCardMapper {
    ScoreCard toModel(ScoreCardResource resource);
    ScoreCard toModel(NewScoreCardResource resource);
    ScoreCardResource toResource(ScoreCard model);
    ScoreCard editModel(ScoreCard source, @MappingTarget ScoreCard target);
}