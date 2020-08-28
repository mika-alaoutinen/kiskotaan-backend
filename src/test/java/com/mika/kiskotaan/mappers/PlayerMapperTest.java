package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.mappers.player.PlayerMapper;
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
    public void shouldMapToModel() {
        Player model = mapper.toModel(TestResources.playerResource());
        assertMappingOk(model, TestResources.playerResource());
    }

    @Test
    public void shouldMapToResource() {
        PlayerResource resource = mapper.toResource(TestModels.player());
        assertMappingOk(TestModels.player(), resource);
    }

    private void assertMappingOk(Player model, PlayerResource resource) {
        assertEquals(model.getId().toString(), resource.getId());
        assertEquals(model.getName(), resource.getName());
    }
}
