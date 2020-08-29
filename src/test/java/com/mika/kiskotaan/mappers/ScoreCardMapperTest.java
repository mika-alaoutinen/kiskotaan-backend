package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.*;
import com.mika.kiskotaan.testdata.TestModels;
import com.mika.kiskotaan.testdata.TestResources;
import kiskotaan.openapi.model.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ScoreCardMapperTest {
    private final CourseMapperTest courseMapperTest = new CourseMapperTest();
    private final PlayerMapperTest playerMapperTest = new PlayerMapperTest();
    private final ScoreMapperTest scoreMapperTest = new ScoreMapperTest();

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
        ScoreCardResource resource = mapper.toResource(TestModels.scoreCard());
        System.out.println(TestModels.scoreCard());
        System.out.println(resource);
        assertMappingOk(TestModels.scoreCard(), resource);
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
        for (int i = 0; i < models.size(); i++) {
            scoreMapperTest.assertScoreRowMappingOk(models.get(i), resources.get(i));
        }
    }
}
