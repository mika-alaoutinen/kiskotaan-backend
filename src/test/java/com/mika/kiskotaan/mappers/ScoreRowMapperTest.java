package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.ScoreRow;
import com.mika.kiskotaan.testdata.TestModels;
import com.mika.kiskotaan.testdata.TestResources;
import com.mika.kiskotaan.utils.MappingAssertions;
import kiskotaan.openapi.model.ScoreRowResource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ScoreRowMapperTest {

    private static final ScoreRow SCORE_ROW = TestModels.scoreRow(1);
    private static final ScoreRowResource SCORE_ROW_RESOURCE = TestResources.scoreRowResource(2);

    @Autowired private ScoreRowMapper mapper;

    @Test
    public void shouldMapScoreRowToModel() {
        ScoreRow mapped = mapper.toModel(SCORE_ROW_RESOURCE);
        MappingAssertions.assertScoreRowMapping(mapped, SCORE_ROW_RESOURCE);
    }

    @Test
    public void shouldMapScoreRowToResource() {
        ScoreRowResource mapped = mapper.toResources(SCORE_ROW);
        MappingAssertions.assertScoreRowMapping(SCORE_ROW, mapped);
    }

    @Test
    public void shouldEditScoreRow() {
        ScoreRow target = new ScoreRow();
        mapper.editScoreRow(SCORE_ROW_RESOURCE, target);
        MappingAssertions.assertScoreRowMapping(target, SCORE_ROW_RESOURCE);
    }
}
