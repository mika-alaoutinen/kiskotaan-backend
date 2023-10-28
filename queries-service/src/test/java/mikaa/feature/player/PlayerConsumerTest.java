package mikaa.feature.player;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySource;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.domain.PlayerEvent;
import mikaa.kiskotaan.domain.PlayerPayload;
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
    var payload = new PlayerPayload(1l, "New", "Player");
    sendEvent(new PlayerEvent(Action.ADD, payload));
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
    when(repository.findByExternalId(1)).thenReturn(AKU_UNI);
    sendDeleteEvent();
    verify(repository, atLeastOnce()).delete(any(PlayerEntity.class));
  }

  @Test
  void does_nothing_on_delete_if_player_not_found() {
    sendDeleteEvent();
    verify(repository, never()).delete(any(PlayerEntity.class));
  }

  @Test
  void ignores_unknown_event_types() {
    sendEvent(new PlayerEvent(Action.UNKNOWN, null));
    verifyNoInteractions(repository);
  }

  private void sendEvent(PlayerEvent event) {
    InMemorySource<PlayerEvent> source = connector.source(IncomingChannels.PLAYER_STATE);
    source.send(event);
  }

  private void sendUpdateEvent() {
    var payload = new PlayerPayload(1l, "Aku", "Ankka");
    sendEvent(new PlayerEvent(Action.UPDATE, payload));
  }

  private void sendDeleteEvent() {
    var payload = new PlayerPayload(1l, "Aku", "Ankka");
    sendEvent(new PlayerEvent(Action.DELETE, payload));
  }

}
