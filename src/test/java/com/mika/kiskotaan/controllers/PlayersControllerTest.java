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
    private static final String URL = "/players";
    private static final Long ID = 1L;

    @MockBean
    private PlayerRepository repository;

    @Test
    public void shouldGetPlayers() throws Exception {
        when(repository.findAll()).thenReturn(TestModels.players());

        MvcResult result = performGet(URL);
        List<Player> players = parsePlayers(result);

        assertPlayersAreSame(players.get(0), TestModels.players().get(0));
        assertPlayersAreSame(players.get(1), TestModels.players().get(1));
        verify(repository, times(1)).findAll();
    }

    @Test
    public void shouldGetPlayer() throws Exception {
        when(repository.findById(ID)).thenReturn(Optional.of(TestModels.player()));

        MvcResult result = performGet(URL + "/" + ID);
        Player player = parsePlayer(result);

        assertPlayersAreSame(player, TestModels.player());
        verify(repository, times(1)).findById(ID);
    }

    @Test
    public void shouldAddPlayer() throws Exception {
        Object resource = TestResources.newPlayerResource();
        Player player = TestModels.player();
        when(repository.save(any(Player.class))).thenReturn(player);

        MvcResult result = performPost(URL, resource);
        Player response = parsePlayer(result);

        assertPlayersAreSame(player, response);
        verify(repository, times(1)).save(any(Player.class));
    }

    @Test
    public void shouldDeletePlayer() throws Exception {
        doNothing().when(repository).deleteById(ID);
        performDelete(URL + "/" + ID);
        verify(repository, times(1)).deleteById(ID);
    }

    public void assertPlayersAreSame(Player p1, Player p2) {
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
