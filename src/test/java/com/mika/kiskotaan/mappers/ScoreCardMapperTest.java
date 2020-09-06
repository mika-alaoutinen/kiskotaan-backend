package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.*;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    public void shouldMapNewResourceToScoreCard() {
        final Course course = TestModels.course();
        final var players = TestModels.players();
        ScoreCard mapped = mapper.toScoreCard(course, players);
        assertNewResourceMappingOk(mapped);
    }
    
    private void assertMappingOk(final ScoreCard model, final ScoreCardResource resource) {
        MappingAssertions.assertCourseMapping(model.getCourse(), resource.getCourse());
        assertPlayersOk(model.getPlayers(), new ArrayList<>(resource.getPlayers()));
        assertScoreRowsOk(model.getRows(), resource.getRows());
    }

    private void assertNewResourceMappingOk(ScoreCard mapped) {
        final Course expectedCourse = TestModels.course();
        final var expectedPlayers = TestModels.players();

        assertEquals(expectedCourse, mapped.getCourse());
        assertEquals(expectedPlayers, mapped.getPlayers());
        assertScoreRowsInitiated(mapped.getRows());
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

    private void assertScoreRowsInitiated(final List<ScoreRow> rows) {
        var expected = TestModels.scoreCard().getRows();
        assertEquals(expected, rows);
    }

    private ScoreRow createRow(final int hole, final int par) {
        var scores = List.of(
                new Score(1L, par),
                new Score(2L, par)
        );

        return new ScoreRow(hole, scores);
    }
}
