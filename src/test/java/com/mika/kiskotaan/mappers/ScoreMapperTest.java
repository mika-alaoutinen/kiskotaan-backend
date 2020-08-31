package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.Score;
import com.mika.kiskotaan.models.ScoreRow;
import com.mika.kiskotaan.testdata.TestModels;
import kiskotaan.openapi.model.ScoreResource;
import kiskotaan.openapi.model.ScoreRowResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ScoreMapperTest {

    @Autowired
    private ScoreRowMapper mapper;

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
