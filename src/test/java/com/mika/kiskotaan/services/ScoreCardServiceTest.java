package com.mika.kiskotaan.services;

import com.mika.kiskotaan.errors.badrequest.ScoreCardException;
import com.mika.kiskotaan.errors.notfound.NotFoundException;
import com.mika.kiskotaan.mappers.ScoreCardMapper;
import com.mika.kiskotaan.models.Course;
import com.mika.kiskotaan.models.Player;
import com.mika.kiskotaan.models.ScoreCard;
import com.mika.kiskotaan.repositories.ScoreCardRepository;
import com.mika.kiskotaan.services.impl.ScoreCardServiceImpl;
import com.mika.kiskotaan.testdata.TestModels;
import com.mika.kiskotaan.testdata.TestResources;
import com.mika.kiskotaan.validators.impl.ScoreCardResourceValidatorImpl;
import kiskotaan.openapi.model.NewScoreCardResource;
import kiskotaan.openapi.model.ScoreCardResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScoreCardServiceTest {

    private static final Long ID = 1L;
    private static final ScoreCard SCORE_CARD = TestModels.scoreCard();
    private static final ScoreCardResource SCORE_CARD_RESOURCE = TestResources.scoreCardResource();

    @Mock private CourseService courseService;
    @Mock private PlayerService playerService;
    @Mock private ScoreCardMapper mapper;
    @Mock private ScoreCardRepository repository;
    @Mock private ScoreCardResourceValidatorImpl validator;
    @InjectMocks private ScoreCardServiceImpl service;

    @Test
    public void shouldGetScoreCard() {
        when(repository.findById(ID)).thenReturn(Optional.of(SCORE_CARD));
        when(mapper.toResource(SCORE_CARD)).thenReturn(SCORE_CARD_RESOURCE);

        service.getScoreCard(ID);
        verify(repository, times(1)).findById(ID);
        verify(mapper, times(1)).toResource(SCORE_CARD);
    }

    @Test
    public void shouldHandleNotFoundOnGet() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () ->
                service.getScoreCard(ID));

        assertNotFoundException(e);
    }

    @Test
    public void shouldAddScoreCards() {
        final Long courseId = 1L;
        final Course course = new Course();
        final List<Player> players = List.of(new Player(), new Player());
        final NewScoreCardResource givenResource = TestResources.newScoreCardResource();
        final ScoreCard scoreCard = new ScoreCard();

        when(validator.validateNewResource(givenResource)).thenReturn(givenResource);
        when(courseService.getCourse(courseId)).thenReturn(course);
        when(playerService.getPlayers(anyList())).thenReturn(players);
        when(mapper.toScoreCard(course, players)).thenReturn(scoreCard);
        when(repository.save(scoreCard)).thenReturn(scoreCard);
        when(mapper.toResource(scoreCard)).thenReturn(SCORE_CARD_RESOURCE);

        ScoreCardResource savedResource = service.addScoreCard(givenResource);
        assertNotNull(savedResource);

        verify(validator, times(1)).validateNewResource(givenResource);
        verify(courseService, times(1)).getCourse(courseId);
        verify(playerService, times(1)).getPlayers(anyList());
        verify(mapper, times(1)).toScoreCard(course, players);
        verify(repository, times(1)).save(scoreCard);
        verify(mapper, times(1)).toResource(scoreCard);
    }

    @Test
    public void shouldNotAddScoreCardWithInvalidCourse() {
        final NewScoreCardResource invalid = new NewScoreCardResource();
        when(validator.validateNewResource(invalid)).thenThrow(new ScoreCardException(new Course()));

        ScoreCardException e = assertThrows(ScoreCardException.class, () ->
                service.addScoreCard(invalid));

        assertEquals("New score card contains a course that does not exist in database.", e.getMessage());
        assertNewScoreCardNotAdded(invalid);
    }

    @Test
    public void shouldNotAddScoreCardWithInvalidPlayers() {
        final NewScoreCardResource invalid = new NewScoreCardResource();
        when(validator.validateNewResource(invalid)).thenThrow(new ScoreCardException(new Player()));

        ScoreCardException e = assertThrows(ScoreCardException.class, () ->
                service.addScoreCard(invalid));

        assertEquals("New score card contains one or more players that do not exist in database.", e.getMessage());
        assertNewScoreCardNotAdded(invalid);
    }

    @Test
    public void shouldDeleteScoreCards() {
        service.deleteScoreCard(ID);
        verify(repository, times(1)).deleteById(ID);
    }

    private void assertNotFoundException(NotFoundException e) {
        assertEquals("Could not find score card with ID " + ID, e.getMessage());
        verify(repository, times(1)).findById(ID);
        verify(mapper, times(0)).toResource(any(ScoreCard.class));
    }

    private void assertNewScoreCardNotAdded(NewScoreCardResource resource) {
        verify(validator, times(1)).validateNewResource(resource);
        verify(courseService, times(0)).getCourse(anyLong());
        verify(playerService, times(0)).getPlayers(anyList());
        verify(mapper, times(0)).toScoreCard(any(Course.class), anyList());
        verify(repository, times(0)).save(any(ScoreCard.class));
        verify(mapper, times(0)).toResource(any(ScoreCard.class));
    }
}
