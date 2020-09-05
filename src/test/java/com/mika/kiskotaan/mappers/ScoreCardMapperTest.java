package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.Player;
import com.mika.kiskotaan.models.Score;
import com.mika.kiskotaan.models.ScoreCard;
import com.mika.kiskotaan.models.ScoreRow;
import com.mika.kiskotaan.testdata.TestModels;
import com.mika.kiskotaan.testdata.TestResources;
import com.mika.kiskotaan.utils.MappingAssertions;
import kiskotaan.openapi.model.NewScoreCardResource;
import kiskotaan.openapi.model.PlayerResource;
import kiskotaan.openapi.model.ScoreCardResource;
import kiskotaan.openapi.model.ScoreRowResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ScoreCardMapperTest {

    private static final ScoreCard SCORE_CARD = TestModels.scoreCard();
    private static final NewScoreCardResource NEW_SCORE_CARD_RESOURCE = TestResources.newScoreCardResource();
    private static final ScoreCardResource SCORE_CARD_RESOURCE = TestResources.scoreCardResource();

    @Autowired private ScoreCardMapper mapper;

    @Test
    public void shouldMapNewScoreCardToModel() {
        ScoreCard mapped = mapper.toModel(NEW_SCORE_CARD_RESOURCE);
        assertMappingOk(mapped);
    }

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

    private void assertMappingOk(ScoreCard model) {
        MappingAssertions.assertCourseMapping(model.getCourse(), NEW_SCORE_CARD_RESOURCE.getCourse());
        assertPlayersOk(model.getPlayers(), NEW_SCORE_CARD_RESOURCE.getPlayers());
        assertScoreRowsInitiated(model.getRows());
    }

    private void assertMappingOk(ScoreCard model, ScoreCardResource resource) {
        MappingAssertions.assertCourseMapping(model.getCourse(), resource.getCourse());
        assertPlayersOk(model.getPlayers(), new ArrayList<>(resource.getPlayers()));
        assertScoreRowsOk(model.getRows(), resource.getRows());
    }

    private void assertPlayersOk(List<Player> models, List<PlayerResource> resources) {
        for (int i = 0; i < models.size(); i++) {
            MappingAssertions.assertPlayerMapping(models.get(i), resources.get(i));
        }
    }

    private void assertScoreRowsOk(List<ScoreRow> models, List<ScoreRowResource> resources) {
        for (int i = 0; i < models.size(); i++) {
            MappingAssertions.assertScoreRowMapping(models.get(i), resources.get(i));
        }
    }

    private void assertScoreRowsInitiated(List<ScoreRow> rows) {
        // Tests ScoreCardMapperDecorator
        List<ScoreRow> expected = IntStream.rangeClosed(1, 18)
                .mapToObj(hole -> createRow(hole, 3))
                .collect(Collectors.toList());

        assertEquals(expected, rows);
    }

    private ScoreRow createRow(int hole, int par) {
        List<Score> scores = List.of(
                new Score(1L, par),
                new Score(2L, par)
        );

        return new ScoreRow(hole, scores);
    }
}
