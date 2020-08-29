package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.Course;
import com.mika.kiskotaan.models.Player;
import com.mika.kiskotaan.models.ScoreCard;
import com.mika.kiskotaan.models.ScoreRow;
import com.mika.kiskotaan.testdata.TestResources;
import kiskotaan.openapi.model.*;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreCardMapperTest {
    private final CourseMapperTest courseMapperTest = new CourseMapperTest();
    private final PlayerMapperTest playerMapperTest = new PlayerMapperTest();

    private final ScoreCardMapper mapper = Mappers.getMapper(ScoreCardMapper.class);

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
        assertScoreRowsOk(model.getScoreRows(), resource.getRows());
    }

    private void assertCourseMappingOk(Course model, CourseResource resource) {
        courseMapperTest.assertCourseMappingMappingOk(model, resource);
    }

    private void assertPlayersOk(List<Player> models, List<PlayerResource> resources) {
        for (int i = 0; i < models.size(); i++) {
            playerMapperTest.assertMappingOk(models.get(i), resources.get(i));
        }
    }

    private void assertScoreRowsOk(Map<Integer, ScoreRow> models, List<ScoreRowResource> resources) {

    }
}
