package com.mika.kiskotaan.dao;

import com.mika.kiskotaan.dao.impl.PlayerDaoImpl;
import com.mika.kiskotaan.repositories.PlayerRepository;
import com.mika.kiskotaan.testdata.TestModels;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerDaoTest {

    private static final Long ID = 1L;

    @Mock private PlayerRepository repository;
    @InjectMocks private PlayerDaoImpl dao;

    @Test
    public void getPlayers() {
        var playerIds = List.of(1L, 2L);
        when(repository.findAllById(playerIds)).thenReturn(TestModels.players());

        var players = dao.getPlayersByIds(playerIds);
        assertEquals(2, players.size());
        verify(repository, times(1)).findAllById(playerIds);
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
}
