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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

@ContextConfiguration(classes = { PlayersController.class, PlayersService.class })
@ExtendWith(SpringExtension.class)
@WebMvcTest(value = PlayersController.class)
class PlayersControllerTest {

  private static final String ENDPOINT = "/players";

  @MockBean
  private PlayersRepository repository;

  @Inject
  private MockMvc mvc;

  @Test
  void should_get_all_players() throws Exception {
    when(repository.findAll()).thenReturn(List.of(new Player("Pekka", "Kana")));

    mvc
      .perform(get(ENDPOINT))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$", hasSize(1)));
  }
}
