package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.Hole;
import kiskotaan.openapi.model.HoleResource;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED
)
public interface HoleMapper {
    Hole toModel(HoleResource resource);
    HoleResource toResource(Hole model);
}
