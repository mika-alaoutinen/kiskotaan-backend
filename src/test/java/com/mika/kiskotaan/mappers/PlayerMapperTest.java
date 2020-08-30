package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.Player;
import com.mika.kiskotaan.testdata.TestModels;
import com.mika.kiskotaan.testdata.TestResources;
import kiskotaan.openapi.model.PlayerResource;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlayerMapperTest {
    private final PlayerMapper mapper = Mappers.getMapper(PlayerMapper.class);

    @Test
    public void shouldMapPlayerToModel() {
        Player model = mapper.toModel(TestResources.playerResource());
        assertMappingOk(model, TestResources.playerResource());
    }

    @Test
    public void shouldMapPlayerToResource() {
        PlayerResource resource = mapper.toResource(TestModels.player());
        assertMappingOk(TestModels.player(), resource);
    }

    public void assertMappingOk(Player model, PlayerResource resource) {
        assertEquals(model.getId(), resource.getId().longValue());
        assertEquals(model.getName(), resource.getName());
    }
}
