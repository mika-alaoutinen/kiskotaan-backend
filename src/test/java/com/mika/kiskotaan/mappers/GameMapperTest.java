package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.Game;
import com.mika.kiskotaan.testdata.TestModels;
import com.mika.kiskotaan.testdata.TestResources;
import com.mika.kiskotaan.utils.MappingAssertions;
import kiskotaan.openapi.model.GameResource;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameMapperTest {
    private static final Game GAME = TestModels.game();
    private static final GameResource RESOURCE = TestResources.gameResource();

    private final GameMapper mapper = Mappers.getMapper(GameMapper.class);

    @Test
    public void shouldMapToModel() {
        Game mapped = mapper.toModel(RESOURCE);
        MappingAssertions.assertGameMapping(mapped, RESOURCE);
    }

    @Test
    public void shouldMapToResource() {
        GameResource mapped = mapper.toResource(GAME);
        MappingAssertions.assertGameMapping(GAME, mapped);
        assertEquals(mapped.getScoreCardId().longValue(), GAME.getScoreCard().getId());
    }
}
