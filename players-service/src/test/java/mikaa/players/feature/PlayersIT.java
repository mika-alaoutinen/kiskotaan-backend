package mikaa.players.feature;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import mikaa.kiskotaan.domain.PlayerPayload;
import mikaa.model.NewPlayerDTO;
import mikaa.players.consumers.PlayerConsumer;
import mikaa.players.testcontainers.Images;
import mikaa.players.utils.MvcUtils;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
class PlayersIT {

  private static final String ENDPOINT = "/players";

  @Autowired
  private PlayerConsumer consumer;

  @Autowired
  private PlayersRepository repository;

  @Autowired
  private MockMvc mvc;

  @Container
  static GenericContainer<?> apicurio = new GenericContainer<>(Images.apicurio).withExposedPorts(8080);

  @Container
  static KafkaContainer kafka = new KafkaContainer(Images.kafka).withKraft();

  @DynamicPropertySource
  static void containerProperties(DynamicPropertyRegistry registry) {
    var port = apicurio.getMappedPort(8080);
    var apicurioUrl = "http://localhost:%s/apis/registry/v2".formatted(port);
    registry.add("apicurio-url", () -> apicurioUrl);
    registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
  }

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
    var payload = consumer.getPlayerAddedAwait();
    assertEventPayload(payload, "Pekka", "Kana");
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
    var payload = consumer.getPlayerUpdatedAwait();
    assertEventPayload(payload, "Edited", "Player");
  }

  @Test
  void should_delete_player() throws Exception {
    var saved = repository.save(new PlayerEntity("Aku", "Ankka"));

    mvc
        .perform(delete(ENDPOINT + "/" + saved.getId()))
        .andExpect(status().isNoContent());

    assertTrue(repository.findById(saved.getId()).isEmpty());
    var payload = consumer.getPlayerDeletedAwait();
    assertEventPayload(payload, "Aku", "Ankka");
  }

  private static void assertEventPayload(PlayerPayload player, String firstName, String lastName) {
    assertEquals(firstName, player.getFirstName().toString());
    assertEquals(lastName, player.getLastName().toString());
  }

}
