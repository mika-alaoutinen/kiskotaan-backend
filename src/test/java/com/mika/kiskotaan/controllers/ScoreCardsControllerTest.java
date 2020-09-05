package com.mika.kiskotaan.controllers;

import com.mika.kiskotaan.models.Player;
import com.mika.kiskotaan.models.Score;
import com.mika.kiskotaan.models.ScoreCard;
import com.mika.kiskotaan.models.ScoreRow;
import com.mika.kiskotaan.repositories.CourseRepository;
import com.mika.kiskotaan.repositories.PlayerRepository;
import com.mika.kiskotaan.repositories.ScoreCardRepository;
import com.mika.kiskotaan.testdata.TestModels;
import com.mika.kiskotaan.testdata.TestResources;
import kiskotaan.openapi.model.ScoreResource;
import kiskotaan.openapi.model.ScoreRowResource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ScoreCardsControllerTest extends ControllerTest {
    private static final String URL = "/scoreCards";
    private static final Long ID = 1L;
    private static final ScoreCard SCORE_CARD = TestModels.scoreCard();

    private final CoursesControllerTest coursesTest = new CoursesControllerTest();
    private final PlayersControllerTest playersTest = new PlayersControllerTest();

    @MockBean private ScoreCardRepository repository;
    @MockBean private CourseRepository courseRepository;
    @MockBean private PlayerRepository playerRepository;

    @Test
    public void shouldGetScoreCard() throws Exception {
        when(repository.findById(ID)).thenReturn(Optional.of(SCORE_CARD));

        MvcResult result = performGet(URL + "/" + ID);

        assertScoreCardsAreSame(parseScoreCard(result));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    public void shouldAddScoreCard() throws Exception {
        Object resource = TestResources.newScoreCardResource();
        Long courseId = 1L;
        Set<Long> playerIds = Set.of(1L, 2L);

        when(courseRepository.existsById(courseId)).thenReturn(true);
        when(playerRepository.existsAllByIdIn(playerIds)).thenReturn(true);
        when(repository.save(any(ScoreCard.class))).thenReturn(SCORE_CARD);

        MvcResult result = performPost(URL, resource);

        assertScoreCardsAreSame(parseScoreCard(result));
        verify(repository, times(1)).save(any(ScoreCard.class));
    }

    @Test
    public void shouldDeleteScoreCard() throws Exception {
        doNothing().when(repository).deleteById(ID);
        performDelete(URL + "/" + ID);
        verify(repository, times(1)).deleteById(ID);
    }

    @Test
    public void shouldUpdateScores() throws Exception {
        ScoreCard scoreCard = TestModels.scoreCard();
        ScoreRowResource resource = TestResources.scoreRowResource(5);
        when(repository.findById(ID)).thenReturn(Optional.of(scoreCard));
        when(repository.save(any(ScoreCard.class))).thenReturn(scoreCard);

        MvcResult result = performPut(URL + "/" + ID + "/scores", resource);
        ScoreRow row = parseScoreRow(result);
        assertScoreRowsAreSame(row, resource);

        verify(repository, times(1)).findById(ID);
        verify(repository, times(1)).save(any(ScoreCard.class));
    }

    private void assertScoreCardsAreSame(ScoreCard card) {
        coursesTest.assertCoursesAreSame(SCORE_CARD.getCourse(), card.getCourse());
        assertPlayersAreSame(SCORE_CARD.getPlayers(), card.getPlayers());
        assertRowsAreSame(SCORE_CARD.getRows(), card.getRows());
    }

    private void assertScoreRowsAreSame(ScoreRow model, ScoreRowResource resource) {
        assertEquals(model.getHole(),  resource.getHole());

        for (int i = 0; i < model.getScores().size(); i++) {
            Score score = model.getScores().get(i);
            ScoreResource scoreResource = resource.getScores().get(i);
            assertEquals(score.getPlayerId(), scoreResource.getPlayerId().longValue());
            assertEquals(score.getScore(), scoreResource.getScore());
        }
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

    private ScoreRow parseScoreRow(MvcResult result) throws Exception {
        return mapper.readValue(testUtils.parseResponseString(result), ScoreRow.class);
    }
}
