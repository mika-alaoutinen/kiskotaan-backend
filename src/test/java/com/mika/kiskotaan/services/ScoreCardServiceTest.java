package com.mika.kiskotaan.services;

import com.mika.kiskotaan.errors.notfound.NotFoundException;
import com.mika.kiskotaan.mappers.ScoreCardMapper;
import com.mika.kiskotaan.models.ScoreCard;
import com.mika.kiskotaan.repositories.ScoreCardRepository;
import com.mika.kiskotaan.services.impl.ScoreCardServiceImpl;
import com.mika.kiskotaan.testdata.TestModels;
import com.mika.kiskotaan.testdata.TestResources;
import kiskotaan.openapi.model.NewScoreCardResource;
import kiskotaan.openapi.model.ScoreCardResource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ScoreCardServiceTest {

    private static final Long ID = 1L;

    @Mock
    private ScoreCardMapper mapper;

    @Mock
    private ScoreCardRepository repository;

    @InjectMocks
    private ScoreCardServiceImpl service;

    @Test
    public void shouldGetScoreCard() {
        when(repository.findById(ID)).thenReturn(Optional.of(TestModels.scoreCard()));
        when(mapper.toResource(any(ScoreCard.class))).thenReturn(TestResources.scoreCardResource());

        service.getScoreCard(ID);
        verify(repository, times(1)).findById(ID);
        verify(mapper, times(1)).toResource(TestModels.scoreCard());
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
        NewScoreCardResource givenResource = new NewScoreCardResource();
        ScoreCard savedCard = new ScoreCard();

        when(mapper.toModel(givenResource)).thenReturn(new ScoreCard());
        when(repository.save(any(ScoreCard.class))).thenReturn(savedCard);
        when(mapper.toResource(any(ScoreCard.class))).thenReturn(TestResources.scoreCardResource());

        ScoreCardResource savedResource = service.addScoreCard(givenResource);

        assertNotNull(savedResource);
        verify(mapper, times(1)).toModel(givenResource);
        verify(repository, times(1)).save(savedCard);
        verify(mapper, times(1)).toResource(savedCard);
    }

    @Test
    public void shouldEditScoreCards() {
        ScoreCardResource givenResource = new ScoreCardResource();
        ScoreCard existingCard = new ScoreCard();
        ScoreCard mappedCard = new ScoreCard();
        ScoreCard editedCard = new ScoreCard();
        ScoreCard savedCard = new ScoreCard();

        when(repository.findById(ID)).thenReturn(Optional.of(existingCard));
        when(mapper.toModel(givenResource)).thenReturn(mappedCard);
        when(mapper.editModel(mappedCard, existingCard)).thenReturn(editedCard);
        when(repository.save(editedCard)).thenReturn(savedCard);
        when(mapper.toResource(savedCard)).thenReturn(TestResources.scoreCardResource());

        ScoreCardResource savedResource = service.editScoreCard(ID, givenResource);

        assertNotNull(savedResource);
        verify(mapper, times(1)).toModel(givenResource);
        verify(mapper, times(1)).editModel(mappedCard, existingCard);
        verify(repository, times(1)).save(savedCard);
        verify(mapper, times(1)).toResource(savedCard);
    }

    @Test
    public void shouldHandleNotFoundOnEdit() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () ->
                service.editScoreCard(ID, new ScoreCardResource()));

        assertNotFoundException(e);
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
