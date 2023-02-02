package mikaa.players.feature;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import mikaa.model.NewPlayerDTO;
import mikaa.players.errors.BadRequestException;
import mikaa.players.events.PlayerEvents.PlayerEvent;
import mikaa.players.infra.GlobalExceptionHandler;
import mikaa.players.kafka.KafkaProducer;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@ContextConfiguration(classes = {
    GlobalExceptionHandler.class,
    PlayersController.class,
    PlayersService.class,
    PlayerValidator.class })
@ExtendWith(SpringExtension.class)
@WebMvcTest
class PlayersControllerTest {

  private static final String ENDPOINT = "/players";
  private static final ObjectMapper MAPPER = new ObjectMapper();
  private static final PlayerEntity PLAYER = new PlayerEntity(1L, "Pekka", "Kana");

  @MockBean
  private KafkaProducer producer;

  @MockBean
  private PlayersRepository repository;

  @Autowired
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
  void get_should_return_404_when_player_not_found() throws Exception {
    when(repository.findById(anyLong())).thenReturn(Optional.empty());

    mvc
        .perform(get(ENDPOINT + "/1"))
        .andExpect(status().isNotFound());
  }

  @Test
  void should_add_new_player() throws Exception {
    when(repository.save(any(PlayerEntity.class))).thenReturn(PLAYER);

    var newPlayer = new NewPlayerDTO().firstName("Pekka").lastName("Kana");

    mvc
        .perform(post(ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJson(newPlayer)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.firstName").value("Pekka"))
        .andExpect(jsonPath("$.lastName").value("Kana"));

    verify(producer, atLeastOnce()).send(any(PlayerEvent.class));
  }

  @MethodSource("invalidNewPlayers")
  @NullSource
  @ParameterizedTest
  void should_not_add_player_if_malformed_payload(NewPlayerDTO invalidPlayer) throws Exception {
    mvc
        .perform(post(ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJson(invalidPlayer)))
        .andExpect(status().isBadRequest());

    verify(repository, never()).save(any());
    verify(producer, never()).send(any());
  }

  @Test
  void should_not_add_player_if_name_not_unique() throws Exception {
    String errorMessage = "Found existing player with the same name";

    doThrow(new BadRequestException(errorMessage))
        .when(repository)
        .existsPlayerByFirstNameAndLastName(anyString(), anyString());

    var newPlayer = new NewPlayerDTO().firstName("Pekka").lastName("Kana");

    mvc
        .perform(post(ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJson(newPlayer)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(errorMessage));

    verifyNoPersist();
  }

  @Test
  void should_update_player() throws Exception {
    when(repository.findById(anyLong())).thenReturn(Optional.of(PLAYER));
    when(repository.save(any(PlayerEntity.class))).thenReturn(new PlayerEntity(1L, "Edited", "Edited"));

    var editedPlayer = new NewPlayerDTO().firstName("Edited").lastName("Edited");

    mvc
        .perform(put(ENDPOINT + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJson(editedPlayer)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.firstName").value("Edited"))
        .andExpect(jsonPath("$.lastName").value("Edited"));

    verify(producer, atLeastOnce()).send(any(PlayerEvent.class));
  }

  @Test
  void put_should_return_404_when_player_not_found() throws Exception {
    when(repository.findById(anyLong())).thenReturn(Optional.empty());
    
    var editedPlayer = new NewPlayerDTO().firstName("Pekka").lastName("Kana");

    mvc
        .perform(put(ENDPOINT + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJson(editedPlayer)))
        .andExpect(status().isNotFound());

    verifyNoPersist();
  }

  @MethodSource("invalidNewPlayers")
  @NullSource
  @ParameterizedTest
  void should_not_update_player_if_malformed_payload(NewPlayerDTO invalidPlayer) throws Exception {
    mvc
        .perform(put(ENDPOINT + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJson(invalidPlayer)))
        .andExpect(status().isBadRequest());

    verifyNoPersist();
  }

  @Test
  void should_not_update_player_if_name_not_unique() throws Exception {
    String errorMessage = "Found existing player with the same name";

    doThrow(new BadRequestException(errorMessage))
        .when(repository)
        .existsPlayerByFirstNameAndLastName(anyString(), anyString());

    var newPlayer = new NewPlayerDTO().firstName("Pekka").lastName("Kana");

    mvc
        .perform(post(ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJson(newPlayer)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(errorMessage));

    verifyNoPersist();
  }

  @Test
  void should_delete_player_by_id() throws Exception {
    when(repository.findById(anyLong())).thenReturn(Optional.of(PLAYER));
    
    mvc
        .perform(delete(ENDPOINT + "/1"))
        .andExpect(status().isNoContent());
    
    verify(repository, atLeastOnce()).deleteById(1L);
    verify(producer, atLeastOnce()).send(any(PlayerEvent.class));
  }

  @Test
  void should_do_nothing_on_delete_when_id_not_found() throws Exception {
    when(repository.findById(anyLong())).thenReturn(Optional.empty());
    
    mvc
        .perform(delete(ENDPOINT + "/1"))
        .andExpect(status().isNoContent());
    
    verify(repository, never()).deleteById(anyLong());
    verify(producer, never()).send(any());
  }

  private void verifyNoPersist() {
    verify(repository, never()).save(any());
    verify(producer, never()).send(any());
  }

  private static Stream<NewPlayerDTO> invalidNewPlayers() {
    return Stream.of(
        new NewPlayerDTO().firstName("Kalle"),
        new NewPlayerDTO().firstName("Kalle").lastName(""));
  }

  private static String asJson(NewPlayerDTO dto) throws JsonProcessingException {
    return MAPPER.writeValueAsString(dto);
  }
}
