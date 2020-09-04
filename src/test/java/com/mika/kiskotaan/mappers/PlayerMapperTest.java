package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.Player;
import com.mika.kiskotaan.testdata.TestModels;
import com.mika.kiskotaan.testdata.TestResources;
import com.mika.kiskotaan.utils.MappingAssertions;
import kiskotaan.openapi.model.NewPlayerResource;
import kiskotaan.openapi.model.PlayerResource;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class PlayerMapperTest {
    private static final NewPlayerResource NEW_PLAYER_RESOURCE = TestResources.newPlayerResource();
    private static final PlayerResource PLAYER_RESOURCE = TestResources.playerResource();
    private static final Player PLAYER = TestModels.player();

    private final PlayerMapper mapper = Mappers.getMapper(PlayerMapper.class);

    @Test
    public void shouldMapPlayerToModel() {
        Player mapped = mapper.toModel(PLAYER_RESOURCE);
        MappingAssertions.assertPlayerMapping(mapped, PLAYER_RESOURCE);
    }

    @Test
    public void shouldMapNewPlayerToModel() {
        Player mapped = mapper.toModel(NEW_PLAYER_RESOURCE);
        MappingAssertions.assertNewPlayerMapping(mapped, NEW_PLAYER_RESOURCE);
    }

    @Test
    public void shouldMapPlayerToResource() {
        PlayerResource mapped = mapper.toResource(PLAYER);
        MappingAssertions.assertPlayerMapping(PLAYER, mapped);
    }
}
