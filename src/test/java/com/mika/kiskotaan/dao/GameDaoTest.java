package com.mika.kiskotaan.dao;

import com.mika.kiskotaan.dao.impl.GameDaoImpl;
import com.mika.kiskotaan.models.Game;
import com.mika.kiskotaan.repositories.GameRepository;
import com.mika.kiskotaan.testdata.TestModels;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameDaoTest {

    @Mock private GameRepository repository;
    @InjectMocks private GameDaoImpl dao;

    @Test
    public void shouldAddGame() {
        final Game game = TestModels.game();
        when(repository.save(game)).thenReturn(game);

        Game saved = dao.addGame(game);
        assertEquals(saved, game);
        verify(repository, times(1)).save(game);
    }

    @Test
    public void shouldDeleteGame() {
        final Long id = 32L;
        doNothing().when(repository).deleteById(id);

        dao.deleteGame(id);
        verify(repository, times(1)).deleteById(id);
    }
}
