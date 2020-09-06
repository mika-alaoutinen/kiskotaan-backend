package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.Player;
import com.mika.kiskotaan.models.ScoreCard;
import com.mika.kiskotaan.models.ScoreRow;
import com.mika.kiskotaan.testdata.TestModels;
import com.mika.kiskotaan.testdata.TestResources;
import com.mika.kiskotaan.utils.MappingAssertions;
import kiskotaan.openapi.model.PlayerResource;
import kiskotaan.openapi.model.ScoreCardResource;
import kiskotaan.openapi.model.ScoreRowResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ScoreCardMapperTest {

    private static final ScoreCard SCORE_CARD = TestModels.scoreCard();
    private static final ScoreCardResource SCORE_CARD_RESOURCE = TestResources.scoreCardResource();

    @Autowired private ScoreCardMapper mapper;

    @Test
    public void shouldMapScoreCardToModel() {
        ScoreCard mapped = mapper.toModel(SCORE_CARD_RESOURCE);
        assertMappingOk(mapped, SCORE_CARD_RESOURCE);
    }

    @Test
    public void shouldMapScoreCardToResource() {
        // Note that players in score card are sorted alphabetically.
        ScoreCardResource mapped = mapper.toResource(SCORE_CARD);
        assertMappingOk(SCORE_CARD, mapped);
    }

    private void assertMappingOk(final ScoreCard model, final ScoreCardResource resource) {
        MappingAssertions.assertCourseMapping(model.getCourse(), resource.getCourse());
        assertPlayersOk(model.getPlayers(), new ArrayList<>(resource.getPlayers()));
        assertScoreRowsOk(model.getRows(), resource.getRows());
    }

    private void assertPlayersOk(final List<Player> models, final List<PlayerResource> resources) {
        for (int i = 0; i < models.size(); i++) {
            MappingAssertions.assertPlayerMapping(models.get(i), resources.get(i));
        }
    }

    private void assertScoreRowsOk(final List<ScoreRow> models, final List<ScoreRowResource> resources) {
        for (int i = 0; i < models.size(); i++) {
            MappingAssertions.assertScoreRowMapping(models.get(i), resources.get(i));
        }
    }
}
