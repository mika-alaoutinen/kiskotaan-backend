package com.mika.kiskotaan.services;

import com.mika.kiskotaan.errors.notfound.NotFoundException;
import com.mika.kiskotaan.mappers.ScoreCardMapper;
import com.mika.kiskotaan.models.Course;
import com.mika.kiskotaan.models.Player;
import com.mika.kiskotaan.models.ScoreCard;
import com.mika.kiskotaan.repositories.ScoreCardRepository;
import com.mika.kiskotaan.services.impl.ScoreCardServiceImpl;
import com.mika.kiskotaan.testdata.TestModels;
import com.mika.kiskotaan.testdata.TestResources;
import com.mika.kiskotaan.validators.ScoreCardResourceValidator;
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
    @Mock private ScoreCardResourceValidator validator;
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
        Long courseId = 1L;
        Course course = new Course();

        List<Long> playerIds = List.of(2L, 3L);

        Player p1 = new Player();
        p1.setId(playerIds.get(0));

        Player p2 = new Player();
        p2.setId(playerIds.get(1));

        List<Player> players = List.of(p1, p2);

        final NewScoreCardResource givenResource = TestResources.newScoreCardResource();
        final ScoreCard savedCard = new ScoreCard();

        when(validator.validateNewResource(givenResource)).thenReturn(givenResource);
        when(courseService.getCourse(courseId)).thenReturn(course);
        when(playerService.getPlayers(anyList())).thenReturn(players);
        when(mapper.toScoreCard(course, players)).thenReturn(savedCard);

        when(repository.save(savedCard)).thenReturn(savedCard);
        when(mapper.toResource(savedCard)).thenReturn(SCORE_CARD_RESOURCE);

        ScoreCardResource savedResource = service.addScoreCard(givenResource);
        assertNotNull(savedResource);

        verify(validator, times(1)).validateNewResource(givenResource);
        verify(courseService, times(1)).getCourse(courseId);
        verify(playerService, times(1)).getPlayers(anyList());
        verify(mapper, times(1)).toScoreCard(course, players);

        verify(repository, times(1)).save(savedCard);
        verify(mapper, times(1)).toResource(savedCard);
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
}
