package com.mika.kiskotaan.services;

import com.mika.kiskotaan.mappers.player.PlayerMapper;
import com.mika.kiskotaan.models.Player;
import com.mika.kiskotaan.repositories.PlayerRepository;
import com.mika.kiskotaan.services.impl.PlayerServiceImpl;
import com.mika.kiskotaan.testdata.TestModels;
import kiskotaan.openapi.model.PlayerResource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

    @Mock
    private PlayerMapper mapper;

    @Mock
    private PlayerRepository repository;

    @InjectMocks
    private PlayerServiceImpl service;

    @BeforeEach
    public void setup() {

    }

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

    }
}
