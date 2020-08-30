package com.mika.kiskotaan.controllers;

import com.mika.kiskotaan.models.Player;
import com.mika.kiskotaan.models.ScoreCard;
import com.mika.kiskotaan.models.ScoreRow;
import com.mika.kiskotaan.repositories.ScoreCardRepository;
import com.mika.kiskotaan.testdata.TestModels;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ScoreCardsControllerTest extends ControllerTest {
    private static final String URL = "/scoreCards";
    private static final Long ID = 1L;

    private final CoursesControllerTest coursesTest = new CoursesControllerTest();
    private final PlayersControllerTest playersTest = new PlayersControllerTest();
    // TODO: Move updating scores into ScoresController from ScoreCardController
    private final ScoreControllerTest scoreTest = new ScoreControllerTest();

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
            scoreTest.assertRowsAreSame(rows1.get(i), rows2.get(i));
        }
    }

    private ScoreCard parseScoreCard(MvcResult result) throws Exception {
        return mapper.readValue(testUtils.parseResponseString(result), ScoreCard.class);
    }
}
