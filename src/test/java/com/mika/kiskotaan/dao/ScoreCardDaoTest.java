package com.mika.kiskotaan.dao;

import com.mika.kiskotaan.dao.impl.ScoreCardDaoImpl;
import com.mika.kiskotaan.models.Course;
import com.mika.kiskotaan.models.ScoreCard;
import com.mika.kiskotaan.models.ScoreRow;
import com.mika.kiskotaan.repositories.ScoreCardRepository;
import com.mika.kiskotaan.testdata.TestModels;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScoreCardDaoTest {

    private static final Long ID = 100L;
    private static final ScoreCard SCORE_CARD = TestModels.scoreCard();

    @Mock private ScoreCardRepository repository;
    @InjectMocks private ScoreCardDaoImpl dao;

    @Test
    public void shouldGetScoreCard() {
        when(repository.findById(ID)).thenReturn(Optional.of(SCORE_CARD));
        Optional<ScoreCard> scoreCard = dao.getScoreCard(ID);
        assertEquals(SCORE_CARD, scoreCard.orElse(null));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    public void shouldHandleScoreCardNotFound() {
        when(repository.findById(ID)).thenReturn(Optional.empty());
        Optional<ScoreCard> scoreCard = dao.getScoreCard(ID);
        assertTrue(scoreCard.isEmpty());
        verify(repository, times(1)).findById(ID);
    }

    @Test
    public void shouldAddScoreCard() {
        when(repository.save(any(ScoreCard.class))).thenReturn(SCORE_CARD);
        ScoreCard saved = dao.addScoreCard(SCORE_CARD);
        assertEquals(SCORE_CARD, saved);
        verify(repository, times(1)).save(any(ScoreCard.class));
    }

    @Test
    public void shouldUpdateScoreCard() {
        when(repository.save(SCORE_CARD)).thenReturn(SCORE_CARD);
        assertEquals(SCORE_CARD, dao.updateScoreCard(SCORE_CARD));
        verify(repository, times(1)).save(SCORE_CARD);
    }

    @Test
    public void shouldDeleteScoreCard() {
        doNothing().when(repository).deleteById(ID);
        dao.deleteScoreCard(ID);
        verify(repository, times(1)).deleteById(ID);
    }

    @Test
    public void shouldInitiateScoreRows() {
        final Course course = TestModels.course();
        final var players = TestModels.players();

        ScoreCard scoreCard = dao.toScoreCard(course, players);
        assertNewResourceMappingOk(scoreCard);
    }

    private void assertNewResourceMappingOk(ScoreCard mapped) {
        final Course expectedCourse = TestModels.course();
        final var expectedPlayers = TestModels.players();

        assertEquals(expectedCourse, mapped.getCourse());
        assertEquals(expectedPlayers, mapped.getPlayers());
        assertScoreRowsInitiated(mapped.getRows());
    }

    private void assertScoreRowsInitiated(final List<ScoreRow> rows) {
        var expected = TestModels.scoreCard().getRows();
        assertEquals(expected, rows);
    }
}
