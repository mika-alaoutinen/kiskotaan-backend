package com.mika.kiskotaan.mappers;

import com.mika.kiskotaan.models.Score;
import com.mika.kiskotaan.testdata.TestModels;
import com.mika.kiskotaan.testdata.TestResources;
import com.mika.kiskotaan.utils.MappingAssertions;
import kiskotaan.openapi.model.ScoreResource;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

public class ScoreMapperTest {

    private static final Score SCORE = TestModels.scores().get(0);
    private static final ScoreResource SCORE_RESOURCE = TestResources.scores(4).get(0);

    private final ScoreMapper mapper = Mappers.getMapper(ScoreMapper.class);

    @Test
    public void shouldMapScoreToModel() {
        Score mapped = mapper.toModel(SCORE_RESOURCE);
        MappingAssertions.assertScoreMapping(mapped, SCORE_RESOURCE);
    }

    @Test
    public void shouldMapScoreToResource() {
        ScoreResource mapped = mapper.toResource(SCORE);
        MappingAssertions.assertScoreMapping(SCORE, mapped);
    }
}
