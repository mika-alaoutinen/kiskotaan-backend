package com.mika.kiskotaan.controllers;

import com.fasterxml.jackson.databind.type.CollectionType;
import com.mika.kiskotaan.models.Player;
import com.mika.kiskotaan.repositories.PlayerRepository;
import com.mika.kiskotaan.testdata.TestModels;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PlayersControllerTest extends ControllerTest {
    private static final String url = "/players";
    private static final Long id = 1L;

    @MockBean
    private PlayerRepository repository;

    @Test
    public void shouldGetPlayers() throws Exception {
        when(repository.findAll()).thenReturn(TestModels.players());

        MvcResult result = mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        List<Player> players = parsePlayers(result);
        assertPlayersAreSame(players.get(0), TestModels.players().get(0));
        assertPlayersAreSame(players.get(1), TestModels.players().get(1));
    }

    @Test
    public void shouldGetPlayer() throws Exception {
        when(repository.findById(id)).thenReturn(Optional.of(TestModels.player()));

        MvcResult result = mockMvc.perform(get(url + "/" + id)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        Player player = parsePlayer(result);
        assertPlayersAreSame(player, TestModels.player());
    }

    private void assertPlayersAreSame(Player p1, Player p2) {
        assertTrue(new ReflectionEquals(p1).matches(p2));
    }

    private List<Player> parsePlayers(MvcResult result) throws Exception {
        CollectionType collectionType = getCollectionType(Player.class);
        String response = parseResponseString(result);
        return mapper.readValue(response, collectionType);
    }

    private Player parsePlayer(MvcResult result) throws Exception {
        return mapper.readValue(parseResponseString(result), Player.class);
    }
}
