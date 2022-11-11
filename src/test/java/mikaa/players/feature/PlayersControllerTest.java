package mikaa.players.feature;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.inject.Inject;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;

@ContextConfiguration(classes = { PlayersController.class, PlayersService.class })
@ExtendWith(SpringExtension.class)
@WebMvcTest
class PlayersControllerTest {

  private static final String ENDPOINT = "/players";
  private static final Player PLAYER = new Player(1L, "Pekka", "Kana");

  @MockBean
  private PlayersRepository repository;

  @Inject
  private MockMvc mvc;

  @Test
  void should_get_all_players() throws Exception {
    when(repository.findAll()).thenReturn(List.of(PLAYER));

    mvc
        .perform(get(ENDPOINT))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));
  }

  @Test
  void should_get_player_by_id() throws Exception {
    when(repository.findById(anyLong())).thenReturn(Optional.of(PLAYER));

    mvc
        .perform(get(ENDPOINT + "/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L))
        .andExpect(jsonPath("$.firstName").value("Pekka"))
        .andExpect(jsonPath("$.lastName").value("Kana"));
  }

  @Test
  void should_return_404_when_player_not_found() throws Exception {
    when(repository.findById(anyLong())).thenReturn(Optional.empty());

    mvc
        .perform(get(ENDPOINT + "/1"))
        .andExpect(status().isNotFound());
  }
}
