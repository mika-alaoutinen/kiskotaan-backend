package com.mika.kiskotaan.controllers;

import com.mika.kiskotaan.models.Score;
import com.mika.kiskotaan.models.ScoreRow;
import com.mika.kiskotaan.repositories.ScoreCardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ScoreControllerTest {

    private static final Long ID = 1L;

    @MockBean
    private ScoreCardRepository repository;

    @Test
    public void shouldUpdateScores() {
    }

    public void assertRowsAreSame(ScoreRow row1, ScoreRow row2) {
        assertEquals(row1.getHole(), row2.getHole());

        for (int i = 0; i < row1.getScores().size(); i++) {
            Score s1 = row1.getScores().get(i);
            Score s2 = row2.getScores().get(i);
            assertScoresAreSame(s1, s2);
        }
    }

    private void assertScoresAreSame(Score s1, Score s2) {
        assertEquals(s1.getPlayerId(), s2.getPlayerId());
        assertEquals(s1.getScore(), s2.getScore());
    }
}
