package com.mika.kiskotaan.controllers;

import com.fasterxml.jackson.databind.type.CollectionType;
import com.mika.kiskotaan.models.Player;
import com.mika.kiskotaan.repositories.PlayerRepository;
import com.mika.kiskotaan.testdata.TestModels;
import com.mika.kiskotaan.testdata.TestResources;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PlayersControllerTest extends ControllerTest {
    private static final String url = "/players";
    private static final Long id = 1L;

    @MockBean
    private PlayerRepository repository;

    @Test
    public void shouldGetPlayers() throws Exception {
        when(repository.findAll()).thenReturn(TestModels.players());

        MvcResult result = performGet(url);
        List<Player> players = parsePlayers(result);

        assertPlayersAreSame(players.get(0), TestModels.players().get(0));
        assertPlayersAreSame(players.get(1), TestModels.players().get(1));
        verify(repository, times(1)).findAll();
    }

    @Test
    public void shouldGetPlayer() throws Exception {
        when(repository.findById(id)).thenReturn(Optional.of(TestModels.player()));

        MvcResult result = performGet(url + "/" + id);
        Player player = parsePlayer(result);

        assertPlayersAreSame(player, TestModels.player());
        verify(repository, times(1)).findById(id);
    }

    @Test
    public void shouldAddPlayer() throws Exception {
        Player player = TestModels.player();
        Object resource = TestResources.newPlayerResource();
        when(repository.save(any(Player.class))).thenReturn(TestModels.player());

        MvcResult result = performPost(url, resource);
        Player response = parsePlayer(result);

        assertPlayersAreSame(player, response);
        verify(repository, times(1)).save(any(Player.class));
    }

    @Test
    public void shouldDeletePlayer() throws Exception {
        doNothing().when(repository).deleteById(id);
        performDelete(url + "/" + id);
        verify(repository, times(1)).deleteById(id);
    }

    private void assertPlayersAreSame(Player p1, Player p2) {
        assertTrue(new ReflectionEquals(p1).matches(p2));
    }

    private List<Player> parsePlayers(MvcResult result) throws Exception {
        CollectionType collectionType = testUtils.getCollectionType(Player.class);
        String response = testUtils.parseResponseString(result);
        return mapper.readValue(response, collectionType);
    }

    private Player parsePlayer(MvcResult result) throws Exception {
        return mapper.readValue(testUtils.parseResponseString(result), Player.class);
    }
}
