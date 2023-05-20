package mikaa.players.feature;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;

import mikaa.model.NewPlayerDTO;
import mikaa.players.utils.MvcUtils;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class PlayersIT {

  private static final String ENDPOINT = "/players";

  @Autowired
  private PlayersRepository repository;

  @Autowired
  private MockMvc mvc;

  @Test
  void should_get_all_players() throws Exception {
    mvc
        .perform(get(ENDPOINT))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)));
  }

  @Test
  void should_get_one_player_by_id() throws Exception {
    var result = mvc.perform(get(ENDPOINT + "/1"))
        .andExpect(status().isOk());

    MvcUtils.verifyName(result, "Aku", "Ankka");
  }

  @Test
  void should_post_new_player() throws Exception {
    var result = mvc
        .perform(post(ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(MvcUtils.asJson(new NewPlayerDTO("Pekka", "Kana"))))
        .andExpect(status().isCreated());

    MvcUtils.verifyName(result, "Pekka", "Kana");
  }

  @Test
  void should_update_existing_player() throws Exception {
    var saved = repository.save(new PlayerEntity("Kalle", "Kukko"));

    var result = mvc
        .perform(put(ENDPOINT + "/" + saved.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(MvcUtils.asJson(new NewPlayerDTO("Edited", "Player"))))
        .andExpect(status().isOk());

    MvcUtils.verifyName(result, "Edited", "Player");
  }

  @Test
  void should_delete_player() throws Exception {
    var saved = repository.save(new PlayerEntity("Aku", "Ankka"));

    mvc
        .perform(delete(ENDPOINT + "/" + saved.getId()))
        .andExpect(status().isNoContent());

    assertTrue(repository.findById(saved.getId()).isEmpty());
  }

}
