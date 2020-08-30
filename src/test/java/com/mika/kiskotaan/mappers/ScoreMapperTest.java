package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.Score;
import com.mika.kiskotaan.models.ScoreRow;
import com.mika.kiskotaan.testdata.TestModels;
import com.mika.kiskotaan.testdata.TestResources;
import kiskotaan.openapi.model.ScoreResource;
import kiskotaan.openapi.model.ScoreRowResource;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoreMapperTest {
    private final ScoreMapper mapper = Mappers.getMapper(ScoreMapper.class);

    @Test
    public void shouldMapScoreRowToModel() {
        ScoreRow model = mapper.toModel(TestResources.scoreRowResource());
        assertScoreRowMappingOk(model, TestResources.scoreRowResource());
    }

    @Test
    public void shouldMapScoreRowToResource() {
        ScoreRowResource resource = mapper.toResources(TestModels.scoreRow());
        assertScoreRowMappingOk(TestModels.scoreRow(), resource);
    }

    public void assertScoreRowMappingOk(ScoreRow model, ScoreRowResource resource) {
        for (int i = 0; i < model.getScores().size(); i++) {
            Score score = model.getScores().get(i);
            ScoreResource scoreResource = resource.getScores().get(i);
            assertScoresAreSame(score, scoreResource);
        }
    }

    private void assertScoresAreSame(Score model, ScoreResource resource) {
        assertEquals(model.getPlayerId(), resource.getPlayerId().longValue());
        assertEquals(model.getScore(), resource.getScore());
    }
}
