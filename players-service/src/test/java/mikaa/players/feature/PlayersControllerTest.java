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

import mikaa.kiskotaan.player.PlayerPayload;
import mikaa.model.NewPlayerDTO;
import mikaa.players.errors.BadRequestException;
import mikaa.players.infra.GlobalExceptionHandler;
import mikaa.players.producers.PlayerProducer;
import mikaa.players.utils.MvcUtils;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
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
  private static final PlayerEntity PLAYER = new PlayerEntity(1L, "Pekka", "Kana");
  private static final NewPlayerDTO NEW_PLAYER = new NewPlayerDTO("Pekka", "Kana");

  @MockBean
  private PlayerProducer producer;

  @MockBean
  private PlayersRepository repository;

  @Autowired
  private MockMvc mvc;

  @Test
  void should_get_all_players() throws Exception {
    when(repository.findByFirstOrLastname("")).thenReturn(List.of(PLAYER));

    mvc
        .perform(get(ENDPOINT))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));
  }

  @Test
  void should_get_player_by_id() throws Exception {
    when(repository.findById(anyLong())).thenReturn(Optional.of(PLAYER));

    var result = mvc
        .perform(get(ENDPOINT + "/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1L));

    MvcUtils.verifyName(result, "Pekka", "Kana");
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

    var result = mvc
        .perform(post(ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(MvcUtils.asJson(NEW_PLAYER)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value(1));

    MvcUtils.verifyName(result, "Pekka", "Kana");
    verify(producer, atLeastOnce()).playerAdded(any(PlayerPayload.class));
  }

  @MethodSource("invalidNewPlayers")
  @NullSource
  @ParameterizedTest
  void should_not_add_player_if_malformed_payload(NewPlayerDTO invalidPlayer) throws Exception {
    mvc
        .perform(post(ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(MvcUtils.asJson(invalidPlayer)))
        .andExpect(status().isBadRequest());

    verifyNoPersist();
  }

  @Test
  void should_not_add_player_if_name_not_unique() throws Exception {
    String errorMessage = "Found existing player with the same name";

    doThrow(new BadRequestException(errorMessage))
        .when(repository)
        .existsPlayerByFirstNameAndLastName(anyString(), anyString());

    mvc
        .perform(post(ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(MvcUtils.asJson(NEW_PLAYER)))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value(errorMessage));

    verifyNoPersist();
  }

  @Test
  void should_update_player() throws Exception {
    when(repository.findById(anyLong())).thenReturn(Optional.of(PLAYER));
    when(repository.save(any(PlayerEntity.class))).thenReturn(new PlayerEntity(1L, "Edited", "Edited"));

    var editedPlayer = new NewPlayerDTO("Edited", "Edited");

    var result = mvc
        .perform(put(ENDPOINT + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(MvcUtils.asJson(editedPlayer)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));

    MvcUtils.verifyName(result, "Edited", "Edited");
    verify(producer, atLeastOnce()).playerUpdated(any(PlayerPayload.class));
  }

  @Test
  void put_should_return_404_when_player_not_found() throws Exception {
    when(repository.findById(anyLong())).thenReturn(Optional.empty());

    mvc
        .perform(put(ENDPOINT + "/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(MvcUtils.asJson(NEW_PLAYER)))
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
            .content(MvcUtils.asJson(invalidPlayer)))
        .andExpect(status().isBadRequest());

    verifyNoPersist();
  }

  @Test
  void should_not_update_player_if_name_not_unique() throws Exception {
    String errorMessage = "Found existing player with the same name";

    doThrow(new BadRequestException(errorMessage))
        .when(repository)
        .existsPlayerByFirstNameAndLastName(anyString(), anyString());

    mvc
        .perform(post(ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .content(MvcUtils.asJson(NEW_PLAYER)))
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
    verify(producer, atLeastOnce()).playerDeleted(any(PlayerPayload.class));
  }

  @Test
  void should_do_nothing_on_delete_when_id_not_found() throws Exception {
    when(repository.findById(anyLong())).thenReturn(Optional.empty());

    mvc
        .perform(delete(ENDPOINT + "/1"))
        .andExpect(status().isNoContent());

    verify(repository, never()).deleteById(anyLong());
    verifyNoInteractions(producer);
  }

  private void verifyNoPersist() {
    verify(repository, never()).save(any());
    verifyNoInteractions(producer);
  }

  private static Stream<NewPlayerDTO> invalidNewPlayers() {
    return Stream.of(
        new NewPlayerDTO("Kalle", null),
        new NewPlayerDTO("Kalle", ""));
  }

}
