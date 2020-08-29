package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.ScoreRow;
import kiskotaan.openapi.model.ScoreRowResource;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScoreMapper {
    ScoreRow toModel(ScoreRowResource resource);
    ScoreRowResource toResources(ScoreRow model);
}
