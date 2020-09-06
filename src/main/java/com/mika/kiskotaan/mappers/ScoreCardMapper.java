package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.ScoreCard;
import kiskotaan.openapi.model.ScoreCardResource;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        uses = { CourseMapper.class, HoleMapper.class, PlayerMapper.class, ScoreRowMapper.class, ScoreMapper.class }
)
public interface ScoreCardMapper {
    ScoreCard toModel(ScoreCardResource resource);
    ScoreCardResource toResource(ScoreCard model);
}