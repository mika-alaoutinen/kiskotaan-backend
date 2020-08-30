package com.mika.kiskotaan.controllers;

import com.mika.kiskotaan.models.Player;
import com.mika.kiskotaan.models.Score;
import com.mika.kiskotaan.models.ScoreCard;
import com.mika.kiskotaan.models.ScoreRow;
import com.mika.kiskotaan.repositories.ScoreCardRepository;
import com.mika.kiskotaan.testdata.TestModels;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ScoreCardsControllerTest extends ControllerTest {
    private static final String URL = "/scoreCards";
    private static final Long ID = 1L;

    private final CoursesControllerTest coursesTest = new CoursesControllerTest();
    private final PlayersControllerTest playersTest = new PlayersControllerTest();

    @MockBean
    private ScoreCardRepository repository;

    @Test
    public void shouldGetScoreCard() throws Exception {
        when(repository.findById(ID)).thenReturn(Optional.of(TestModels.scoreCard()));

        MvcResult result = performGet(URL + "/" + ID);
        ScoreCard scoreCard = parseScoreCard(result);

        System.out.println(TestModels.scoreCard());
        System.out.println(scoreCard);

        assertScoreCardsAreSame(scoreCard, TestModels.scoreCard());
        verify(repository, times(1)).findById(ID);
    }

    private void assertScoreCardsAreSame(ScoreCard card1, ScoreCard card2) {
        coursesTest.assertCoursesAreSame(card1.getCourse(), card2.getCourse());
        assertPlayersAreSame(card1.getPlayers(), card2.getPlayers());
        assertRowsAreSame(card1.getRows(), card2.getRows());
    }

    private void assertPlayersAreSame(List<Player> players1, List<Player> players2) {
        for (int i = 0; i < players1.size(); i++) {
            playersTest.assertPlayersAreSame(players1.get(i), players2.get(i));
        }
    }

    private void assertRowsAreSame(List<ScoreRow> rows1, List<ScoreRow> rows2) {
        for (int i = 0; i < rows1.size(); i++) {
            rowsAreSame(rows1.get(i), rows2.get(i));
        }
    }

    private void rowsAreSame(ScoreRow row1, ScoreRow row2) {
        assertEquals(row1.getHole(), row2.getHole());

        for (int i = 0; i < row1.getScores().size(); i++) {
            Score s1 = row1.getScores().get(i);
            Score s2 = row2.getScores().get(i);
            scoresAreSame(s1, s2);
        }
    }

    private void scoresAreSame(Score s1, Score s2) {
        assertEquals(s1.getPlayerId(), s2.getPlayerId());
        assertEquals(s1.getScore(), s2.getScore());
    }

    private ScoreCard parseScoreCard(MvcResult result) throws Exception {
        return mapper.readValue(testUtils.parseResponseString(result), ScoreCard.class);
    }
}
