package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.ScoreRow;
import kiskotaan.openapi.model.ScoreRowResource;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.TARGET_IMMUTABLE, // Fixes a controller test case failure
        uses = ScoreMapper.class
)
public interface ScoreRowMapper {
    ScoreRow toModel(ScoreRowResource resource);
    ScoreRowResource toResources(ScoreRow model);
    void editScoreRow(ScoreRowResource source, @MappingTarget ScoreRow target);
}
