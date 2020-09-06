package com.mika.kiskotaan.services;

import com.mika.kiskotaan.dao.PlayerDao;
import com.mika.kiskotaan.errors.badrequest.PlayerException;
import com.mika.kiskotaan.errors.notfound.NotFoundException;
import com.mika.kiskotaan.mappers.PlayerMapper;
import com.mika.kiskotaan.models.Player;
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

    @Mock private PlayerDao dao;
    @Mock private PlayerMapper mapper;
    @InjectMocks private PlayerServiceImpl service;

    @Test
    public void shouldGetPlayers() {
        when(dao.getPlayers()).thenReturn(TestModels.players());

        final List<PlayerResource> players = service.getPlayers();
        assertEquals(2, players.size());
        verify(dao, times(1)).getPlayers();
        verify(mapper, times(2)).toResource(any(Player.class));
    }

    @Test
    public void shouldGetPlayer() {
        when(dao.getPlayer(ID)).thenReturn(Optional.of(TestModels.player()));
        when(mapper.toResource(any(Player.class))).thenReturn(TestResources.playerResource());

        service.getPlayer(ID);
        verify(dao, times(1)).getPlayer(ID);
        verify(mapper, times(1)).toResource(TestModels.player());
    }

    @Test
    public void shouldHandlePlayerNotFound() {
        when(dao.getPlayer(ID)).thenReturn(Optional.empty());

        NotFoundException e = assertThrows(NotFoundException.class, () ->
                service.getPlayer(ID));

        assertEquals("Could not find player with ID " + ID, e.getMessage());
        verify(dao, times(1)).getPlayer(ID);
        verify(mapper, times(0)).toResource(any(Player.class));
    }

    @Test
    public void shouldAddPlayer() {
        final String name = "New player";
        final NewPlayerResource givenResource = new NewPlayerResource().name(name);
        final Player savedPlayer = new Player();

        when(dao.existsByName(name)).thenReturn(false);
        when(mapper.toModel(givenResource)).thenReturn(new Player());
        when(dao.addPlayer(any(Player.class))).thenReturn(savedPlayer);
        when(mapper.toResource(savedPlayer)).thenReturn(TestResources.playerResource());

        PlayerResource savedResource = service.addPlayer(givenResource);
        assertNotNull(savedResource);
        verify(dao, times(1)).existsByName(name);
        verify(mapper, times(1)).toModel(givenResource);
        verify(dao, times(1)).addPlayer(savedPlayer);
        verify(mapper, times(1)).toResource(savedPlayer);
    }

    @Test
    public void shouldNotAddPlayerWithDuplicateName() {
        final String name = "Existing player";
        final NewPlayerResource invalid = new NewPlayerResource().name(name);
        when(dao.existsByName(name)).thenReturn(true);

        PlayerException e = assertThrows(PlayerException.class, () -> service.addPlayer(invalid));
        assertEquals("Player with name Existing player already exists.", e.getMessage());
        verify(dao, times(1)).existsByName(name);
        verify(mapper, times(0)).toModel(any(NewPlayerResource.class));
        verify(dao, times(0)).addPlayer(any(Player.class));
        verify(mapper, times(0)).toResource(any(Player.class));
    }

    @Test
    public void shouldDeletePlayer() {
        service.deletePlayer(ID);
        verify(dao, times(1)).deletePlayer(ID);
    }
}
