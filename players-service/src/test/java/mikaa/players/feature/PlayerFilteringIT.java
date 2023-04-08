package mikaa.players.feature;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
class PlayerFilteringIT {

  @Autowired
  private MockMvc mvc;

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
