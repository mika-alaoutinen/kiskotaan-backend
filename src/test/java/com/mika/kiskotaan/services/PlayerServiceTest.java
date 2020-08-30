package com.mika.kiskotaan.services;

import com.mika.kiskotaan.errors.notfound.NotFoundException;
import com.mika.kiskotaan.mappers.PlayerMapper;
import com.mika.kiskotaan.models.Player;
import com.mika.kiskotaan.repositories.PlayerRepository;
import com.mika.kiskotaan.services.impl.PlayerServiceImpl;
import com.mika.kiskotaan.testdata.TestModels;
import com.mika.kiskotaan.testdata.TestResources;
import kiskotaan.openapi.model.NewPlayerResource;
import kiskotaan.openapi.model.PlayerResource;
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
public class PlayerServiceTest {

    private static final Long ID = 1L;

    @Mock
    private PlayerMapper mapper;

    @Mock
    private PlayerRepository repository;

    @InjectMocks
    private PlayerServiceImpl service;

    @Test
    public void shouldGetPlayers() {
        when(repository.findAll()).thenReturn(TestModels.players());

        List<PlayerResource> players = service.getPlayers();

        assertEquals(2, players.size());
        verify(repository, times(1)).findAll();
        verify(mapper, times(2)).toResource(any(Player.class));
    }

    @Test
    public void shouldGetPlayer() {
        when(repository.findById(ID)).thenReturn(Optional.of(TestModels.player()));
        when(mapper.toResource(any(Player.class))).thenReturn(TestResources.playerResource());

        service.getPlayer(ID);
        verify(repository, times(1)).findById(ID);
        verify(mapper, times(1)).toResource(TestModels.player());
    }

    @Test
    public void shouldHandlePlayerNotFound() {
        when(repository.findById(ID)).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () ->
                service.getPlayer(ID));

        assertEquals("Could not find player with ID " + ID, e.getMessage());
        verify(repository, times(1)).findById(ID);
        verify(mapper, times(0)).toResource(any(Player.class));
    }

    @Test
    public void shouldAddPlayer() {
        NewPlayerResource givenResource = new NewPlayerResource();
        Player savedPlayer = new Player();

        when(mapper.toModel(givenResource)).thenReturn(new Player());
        when(repository.save(any(Player.class))).thenReturn(savedPlayer);
        when(mapper.toResource(savedPlayer)).thenReturn(TestResources.playerResource());

        PlayerResource savedResource = service.addPlayer(givenResource);

        assertNotNull(savedResource);
        verify(mapper, times(1)).toModel(givenResource);
        verify(repository, times(1)).save(savedPlayer);
        verify(mapper, times(1)).toResource(savedPlayer);
    }

    @Test
    public void shouldDeletePlayer() {
        service.deletePlayer(ID);
        verify(repository, times(1)).deleteById(ID);
    }
}
