package com.mika.kiskotaan.dao;

import com.mika.kiskotaan.dao.impl.PlayerDaoImpl;
import com.mika.kiskotaan.models.Player;
import com.mika.kiskotaan.repositories.PlayerRepository;
import com.mika.kiskotaan.testdata.TestModels;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerDaoTest {

    private static final Long ID = 1L;
    private static final List<Player> PLAYERS = TestModels.players();
    private static final Player PLAYER = new Player();

    @Mock private PlayerRepository repository;
    @InjectMocks private PlayerDaoImpl dao;

    @Test
    public void shouldGetPlayers() {
        when(repository.findAll()).thenReturn(PLAYERS);
        assertEquals(PLAYERS.size(), dao.getPlayers().size());
        verify(repository, times(1)).findAll();
    }

    @Test
    public void shouldGetPlayersByIds() {
        var playerIds = List.of(1L, 2L);
        when(repository.findAllById(playerIds)).thenReturn(PLAYERS);
        assertEquals(2, dao.getPlayersByIds(playerIds).size());
        verify(repository, times(1)).findAllById(playerIds);
    }

    @Test
    public void shouldGetPlayer() {
        when(repository.findById(ID)).thenReturn(Optional.of(PLAYER));
        Optional<Player> player = dao.getPlayer(ID);
        assertNotNull(player.orElse(null));
        verify(repository, times(1)).findById(ID);
    }

    @Test
    public void shouldAddPlayer() {
        when(repository.save(PLAYER)).thenReturn(PLAYER);
        assertEquals(PLAYER, dao.addPlayer(PLAYER));
        verify(repository, times(1)).save(PLAYER);
    }

    @Test
    public void shouldDeletePlayer() {
        doNothing().when(repository).deleteById(ID);
        dao.deletePlayer(ID);
        verify(repository, times(1)).deleteById(ID);
    }

    @Test
    public void shouldReturnTrueWhenExistById() {
        when(repository.existsById(anyLong())).thenReturn(true);
        assertTrue(dao.existsByIds(List.of(1L, 2L)));
        verify(repository, times(2)).existsById(anyLong());
    }

    @Test
    public void shouldReturnFalseWhenNotExistById() {
        when(repository.existsById(ID)).thenReturn(false);
        assertFalse(dao.existsByIds(List.of(ID, 2L)));
        verify(repository, times(1)).existsById(anyLong());
    }

    @Test
    public void shouldReturnTrueWhenExistsByName() {
        String name = "Player 1";
        when(repository.existsByNameIgnoreCase(name)).thenReturn(true);
        assertTrue(dao.existsByName(name));
        verify(repository, times(1)).existsByNameIgnoreCase(name);
    }

    @Test
    public void shouldReturnFalseWhenNotExistsByName() {
        String name = "Not found";
        when(repository.existsByNameIgnoreCase(name)).thenReturn(false);
        assertFalse(dao.existsByName(name));
        verify(repository, times(1)).existsByNameIgnoreCase(name);
    }
}
