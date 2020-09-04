package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.Player;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ScoreCardMapperTest {

    @Autowired
    private ScoreCardMapper mapper;

    @Test
    public void shouldMapNewScoreCardToModel() {
        ScoreCard model = mapper.toModel(TestResources.newScoreCardResource());
        assertMappingOk(model, TestResources.newScoreCardResource());
    }

    @Test
    public void shouldMapScoreCardToModel() {
        ScoreCard model = mapper.toModel(TestResources.scoreCardResource());
        assertMappingOk(model, TestResources.scoreCardResource());
    }

    @Test
    public void shouldMapScoreCardToResource() {
        // Note that players in score card are sorted alphabetically.
        ScoreCardResource resource = mapper.toResource(TestModels.scoreCard());
        assertMappingOk(TestModels.scoreCard(), resource);
    }

    @Test
    public void shouldUpdateScoreCard() {
        ScoreCard source = TestModels.scoreCard();
        ScoreCard updated = mapper.editModel(source, new ScoreCard());
        assertEquals(source, updated);
    }

    private void assertMappingOk(ScoreCard model, NewScoreCardResource resource) {
        MappingAssertions.assertCourseMapping(model.getCourse(), resource.getCourse());
        assertPlayersOk(model.getPlayers(), new ArrayList<>(resource.getPlayers()));
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
}
