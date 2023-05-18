package mikaa.feature.player;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySource;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import mikaa.PlayerPayload;
import mikaa.config.IncomingChannels;

@QuarkusTest
class PlayerConsumerTest {

  static final Uni<PlayerEntity> AKU_UNI = Uni.createFrom().item(new PlayerEntity(1l, "Aku", "Ankka"));

  @InjectMock
  PlayerRepository repository;

  @Any
  @Inject
  InMemoryConnector connector;

  @Test
  void handles_add_player_event() {
    InMemorySource<PlayerPayload> source = connector.source(IncomingChannels.Player.PLAYER_ADDED);
    source.send(new PlayerPayload(1l, "New", "Player"));
    verify(repository, atLeastOnce()).persist(any(PlayerEntity.class));
  }

  @Test
  void handles_upate_player_event() {
    when(repository.findByExternalId(1)).thenReturn(AKU_UNI);
    sendUpdateEvent();
    verify(repository, atLeastOnce()).update(any(PlayerEntity.class));
  }

  @Test
  void does_nothing_on_update_if_player_not_found() {
    sendUpdateEvent();
    verify(repository, never()).update(any(PlayerEntity.class));
  }

  @Test
  void handles_delete_player_event() {
    sendDeleteEvent();
    verify(repository, atLeastOnce()).delete(any(PlayerEntity.class));
  }

  @Test
  void does_nothing_on_delete_if_player_not_found() {
    sendDeleteEvent();
    verify(repository, never()).delete(any(PlayerEntity.class));
  }

  private void sendUpdateEvent() {
    InMemorySource<PlayerPayload> source = connector.source(IncomingChannels.Player.PLAYER_UPDATED);
    source.send(new PlayerPayload(1l, "Aku", "Ankka"));
  }

  private void sendDeleteEvent() {
    InMemorySource<PlayerPayload> source = connector.source(IncomingChannels.Player.PLAYER_DELETED);
    source.send(new PlayerPayload(1l, "Aku", "Ankka"));
  }

}
