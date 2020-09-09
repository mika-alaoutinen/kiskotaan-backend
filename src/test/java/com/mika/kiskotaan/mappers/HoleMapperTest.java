package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.Hole;
import com.mika.kiskotaan.testdata.TestModels;
import com.mika.kiskotaan.testdata.TestResources;
import com.mika.kiskotaan.utils.MappingAssertions;
import kiskotaan.openapi.model.HoleResource;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class HoleMapperTest {
    private static final Hole MODEL = TestModels.hole(1);
    private static final HoleResource RESOURCE = TestResources.holeResource(2);

    private final HoleMapper mapper = Mappers.getMapper(HoleMapper.class);

    @Test
    public void shouldMapToModel() {
        Hole mapped = mapper.toModel(RESOURCE);
        MappingAssertions.assertHoleMapping(mapped, RESOURCE);
    }

    @Test
    public void shouldMapToResource() {
        HoleResource mapped = mapper.toResource(MODEL);
        MappingAssertions.assertHoleMapping(MODEL, mapped);
    }
}
