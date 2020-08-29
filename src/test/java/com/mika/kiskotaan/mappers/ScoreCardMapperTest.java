package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.*;
import com.mika.kiskotaan.testdata.TestResources;
import kiskotaan.openapi.model.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ScoreCardMapperTest {
    private final CourseMapperTest courseMapperTest = new CourseMapperTest();
    private final PlayerMapperTest playerMapperTest = new PlayerMapperTest();

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

    private void assertMappingOk(ScoreCard model, NewScoreCardResource resource) {
        assertCourseMappingOk(model.getCourse(), resource.getCourse());
        assertPlayersOk(model.getPlayers(), new ArrayList<>(resource.getPlayers()));
    }

    private void assertMappingOk(ScoreCard model, ScoreCardResource resource) {
        assertCourseMappingOk(model.getCourse(), resource.getCourse());
        assertPlayersOk(model.getPlayers(), new ArrayList<>(resource.getPlayers()));
        assertScoreRowsOk(model.getRows(), resource.getRows());
    }

    private void assertCourseMappingOk(Course model, CourseResource resource) {
        courseMapperTest.assertCourseMappingMappingOk(model, resource);
    }

    private void assertPlayersOk(List<Player> models, List<PlayerResource> resources) {
        for (int i = 0; i < models.size(); i++) {
            playerMapperTest.assertMappingOk(models.get(i), resources.get(i));
        }
    }

    private void assertScoreRowsOk(List<ScoreRow> models, List<ScoreRowResource> resources) {
        System.out.println(models);
        System.out.println(resources);

        for (int i = 0; i < models.size(); i++) {
            List<Score> scores = models.get(i).getScores();
            ScoreRowResource resource = resources.get(i);

            for (int j = 0; j < scores.size(); j++) {
                Score score = scores.get(j);
                ScoreResource scoreResource = resource.getScores().get(j);
                assertScoresAreSame(score, scoreResource);
            }
        }
    }

    private void assertScoresAreSame(Score model, ScoreResource resource) {
        assertEquals(model.getPlayerId(), resource.getPlayerId().longValue());
        assertEquals(model.getScore(), resource.getScore());
    }
}
