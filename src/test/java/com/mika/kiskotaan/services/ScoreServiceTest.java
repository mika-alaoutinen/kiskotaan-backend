package com.mika.kiskotaan.services;

import com.mika.kiskotaan.dao.ScoreCardDao;
import com.mika.kiskotaan.errors.badrequest.ScoreRowException;
import com.mika.kiskotaan.errors.notfound.NotFoundException;
import com.mika.kiskotaan.mappers.ScoreRowMapper;
import com.mika.kiskotaan.models.ScoreCard;
import com.mika.kiskotaan.models.ScoreRow;
import com.mika.kiskotaan.services.impl.ScoreServiceImpl;
import com.mika.kiskotaan.testdata.TestModels;
import com.mika.kiskotaan.testdata.TestResources;
import kiskotaan.openapi.model.ScoreRowResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScoreServiceTest {

    private static final Long ID = 1L;
    private static final ScoreCard SCORE_CARD = TestModels.scoreCard();

    @Mock private ScoreCardDao dao;
    @Mock private ScoreRowMapper mapper;
    @InjectMocks private ScoreServiceImpl service;

    @Test
    public void shouldEditScores() {
        final ScoreRowResource scoreRowResource = createEditedScoreRow();
        final ScoreRow row = SCORE_CARD.getRows().get(0);

        when(dao.getScoreCard(ID)).thenReturn(Optional.of(SCORE_CARD));
        when(dao.updateScoreCard(SCORE_CARD)).thenReturn(SCORE_CARD);

        service.editScoreRow(ID, scoreRowResource);
        verify(dao, times(1)).getScoreCard(ID);
        verify(mapper, times(1)).editScoreRow(scoreRowResource, row);
        verify(dao, times(1)).updateScoreCard(SCORE_CARD);
        verify(mapper, times(1)).toResources(row);
    }

    @Test
    public void shouldHandleNotFound() {
        when(dao.getScoreCard(ID)).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () ->
                service.editScoreRow(ID, createEditedScoreRow()));

        assertThrowsException("Could not find score card with ID 1", e);
    }

    @Test
    public void shouldHandleBadRequest() {
        ScoreRowResource invalid = new ScoreRowResource().hole(19);
        when(dao.getScoreCard(ID)).thenReturn(Optional.of(TestModels.scoreCard()));

        ScoreRowException e = assertThrows(ScoreRowException.class, () ->
                service.editScoreRow(ID, invalid));

        assertThrowsException("Could not edit score row with hole number 19", e);
    }

    private void assertThrowsException(String expectedErrorMessage, Exception e) {
        assertEquals(expectedErrorMessage, e.getMessage());
        verify(dao, times(1)).getScoreCard(ID);
        verify(mapper, times(0)).editScoreRow(any(ScoreRowResource.class), any(ScoreRow.class));
        verify(dao, times(0)).addScoreCard(any(ScoreCard.class));
        verify(mapper, times(0)).toResources(any(ScoreRow.class));
    }

    private ScoreRowResource createEditedScoreRow() {
        return new ScoreRowResource()
                .hole(1)
                .scores(TestResources.scores(6));
    }
}
