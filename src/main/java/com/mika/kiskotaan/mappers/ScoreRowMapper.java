package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.ScoreRow;
import kiskotaan.openapi.model.ScoreRowResource;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = ScoreMapper.class)
public interface ScoreRowMapper {
    ScoreRow toModel(ScoreRowResource resource);
    ScoreRowResource toResources(ScoreRow model);
    void editScoreRow(ScoreRowResource source, @MappingTarget ScoreRow target);
}
