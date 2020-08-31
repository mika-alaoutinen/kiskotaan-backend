package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.ScoreRow;
import kiskotaan.openapi.model.ScoreRowResource;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ScoreMapper {
    ScoreRow toModel(ScoreRowResource resource);
    ScoreRowResource toResources(ScoreRow model);
    ScoreRow editRow(ScoreRowResource source, @MappingTarget ScoreRow target);
}
