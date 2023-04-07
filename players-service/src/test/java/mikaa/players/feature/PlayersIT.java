package mikaa.players.feature;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import mikaa.model.NewPlayerDTO;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class PlayersIT {

  private static final String ENDPOINT = "/players";
  private static final ObjectMapper MAPPER = new ObjectMapper();

  @Autowired
  private PlayersRepository repository;

  @Autowired
  private MockMvc mvc;

  @Test
  void should_post_new_player() throws Exception {
    mvc
        .perform(post(ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJson(new NewPlayerDTO("Pekka", "Kana"))))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.firstName").value("Pekka"))
        .andExpect(jsonPath("$.lastName").value("Kana"));
  }

  @Test
  void should_update_existing_player() throws Exception {
    var saved = repository.save(new PlayerEntity("Kalle", "Kukko"));

    mvc
        .perform(put(ENDPOINT + "/" + saved.getId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJson(new NewPlayerDTO("Edited", "Player"))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.firstName").value("Edited"))
        .andExpect(jsonPath("$.lastName").value("Player"));
  }

  @Test
  void should_delete_player() throws Exception {
    var saved = repository.save(new PlayerEntity("Aku", "Ankka"));

    mvc
        .perform(delete(ENDPOINT + "/" + saved.getId()))
        .andExpect(status().isNoContent());

    assertTrue(repository.findById(saved.getId()).isEmpty());
  }

  private static String asJson(NewPlayerDTO dto) throws JsonProcessingException {
    return MAPPER.writeValueAsString(dto);
  }

}
