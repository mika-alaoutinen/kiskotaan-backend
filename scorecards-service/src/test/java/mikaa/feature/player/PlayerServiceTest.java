package mikaa.feature.player;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import mikaa.kiskotaan.domain.PlayerPayload;

@QuarkusTest
class PlayerServiceTest {

  static final PlayerEntity ENTITY = new PlayerEntity(123, "Mikki", "Hiiri");
  static final PlayerPayload PAYLOAD = new PlayerPayload(123l, "Mikki", "Hiiri");

  @InjectMock
  PlayerRepository repository;

  PlayerService service;

  @BeforeEach
  void setup() {
    service = new PlayerService(repository);
  }

  @Test
  void should_save_new_player() {
    service.add(PAYLOAD);
    verify(repository, atLeastOnce()).persist(ENTITY);
  }

  @Test
  void should_update_player() {
    var player = new PlayerEntity(123L, "Pekka", "Kana");
    when(repository.findByExternalId(anyLong())).thenReturn(Optional.of(player));
    service.update(PAYLOAD);
    verify(repository, atLeastOnce()).persist(ENTITY);
  }

  @Test
  void should_do_nothing_on_update_if_player_not_found() {
    service.update(PAYLOAD);
    verify(repository, never()).persist(any(PlayerEntity.class));
  }

  @Test
  void should_delete_player() {
    when(repository.findByExternalId(anyLong())).thenReturn(Optional.of(ENTITY));
    service.delete(PAYLOAD);
    verify(repository, atLeastOnce()).delete(ENTITY);
  }

}
