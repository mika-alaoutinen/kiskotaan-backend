package mikaa.players.feature;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import mikaa.players.testcontainers.Images;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Testcontainers
class PlayerFilteringIT {

  @Autowired
  private MockMvc mvc;

  @Container
  static KafkaContainer kafka = new KafkaContainer(Images.kafka).withKraft();

  @DynamicPropertySource
  static void kafkaProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
  }

  @ParameterizedTest
  @ValueSource(strings = { "Hessu", "essu", "hes", "opo" })
  void should_filter_players_by_first_name_or_last_name(String nameFilter) throws Exception {
    var result = mvc
        .perform(get(url(nameFilter)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));

    verifyName(result, "Hessu", "Hopo");
  }

  @ParameterizedTest
  @ValueSource(strings = { "Hessu Hopo", "hes opo" })
  void should_filter_players_by_first_and_last_name(String nameFilter) throws Exception {
    var result = mvc
        .perform(get(url(nameFilter)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));

    verifyName(result, "Hessu", "Hopo");
  }

  @Test
  void should_find_aku_and_iines() throws Exception {
    mvc
        .perform(get(url("Ankka")))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].firstName").value("Aku"))
        .andExpect(jsonPath("$[1].firstName").value("Iines"));
  }

  private static void verifyName(ResultActions result, String firstName, String lastName) throws Exception {
    result
        .andExpect(jsonPath("$[0].firstName").value(firstName))
        .andExpect(jsonPath("$[0].lastName").value(lastName));
  }

  private static String url(String nameFilter) {
    return String.format("/players?name=%s", nameFilter);
  }

}
