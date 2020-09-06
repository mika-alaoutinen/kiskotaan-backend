package com.mika.kiskotaan.validators;

import com.mika.kiskotaan.dao.CourseDao;
import com.mika.kiskotaan.errors.badrequest.ScoreCardException;
import com.mika.kiskotaan.services.PlayerService;
import com.mika.kiskotaan.testdata.TestResources;
import com.mika.kiskotaan.validators.impl.ScoreCardResourceValidatorImpl;
import kiskotaan.openapi.model.NewScoreCardResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScoreCardResourceValidatorTest {

    private static final NewScoreCardResource NEW_SCORE_CARD_RESOURCE = TestResources.newScoreCardResource();
    private static final Long COURSE_ID = 1L;
    private static final Collection<Long> PLAYER_IDS = List.of(2L, 3L);

    @Mock private CourseDao courseDao;
    @Mock private PlayerService playerService;
    @InjectMocks private ScoreCardResourceValidatorImpl validator;

    @Test
    public void shouldValidateNewResource() {
        when(courseDao.existsById(COURSE_ID)).thenReturn(true);
        when(playerService.existsByIds(PLAYER_IDS)).thenReturn(true);

        NewScoreCardResource validated = validator.validateNewResource(NEW_SCORE_CARD_RESOURCE);
        assertEquals(NEW_SCORE_CARD_RESOURCE, validated);
        verify(courseDao, times(1)).existsById(COURSE_ID);
        verify(playerService, times(1)).existsByIds(PLAYER_IDS);
    }

    @Test
    public void shouldThrowExceptionIfCourseDoesntExist() {
        when(courseDao.existsById(COURSE_ID)).thenReturn(false);
        assertThrows(ScoreCardException.class, () -> validator.validateNewResource(NEW_SCORE_CARD_RESOURCE));
        verify(courseDao, times(1)).existsById(COURSE_ID);
    }

    @Test
    public void shouldThrowExceptionIfPlayersDontExist() {
        when(courseDao.existsById(COURSE_ID)).thenReturn(true);
        when(playerService.existsByIds(PLAYER_IDS)).thenReturn(false);

        assertThrows(ScoreCardException.class, () -> validator.validateNewResource(NEW_SCORE_CARD_RESOURCE));
        verify(courseDao, times(1)).existsById(COURSE_ID);
        verify(playerService, times(1)).existsByIds(PLAYER_IDS);
    }
}