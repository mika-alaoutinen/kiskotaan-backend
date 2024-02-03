package mikaa.feature.player;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import mikaa.config.IncomingChannels;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.player.PlayerEvent;
import mikaa.kiskotaan.player.PlayerPayload;

@QuarkusTest
class PlayerConsumerTest {

  static final PlayerEntity AKU = new PlayerEntity(1l, "Aku", "Ankka");

  @InjectMock
  PlayerRepository repository;

  @Any
  @Inject
  InMemoryConnector connector;

  @Test
  void handles_add_player_event() throws InterruptedException {
    var payload = new PlayerPayload(1l, "New", "Player");
    sendEvent(new PlayerEvent(Action.ADD, payload));
    verify(repository, atLeastOnce()).persist(any(PlayerEntity.class));
  }

  @Test
  void handles_upate_player_event() throws InterruptedException {
    when(repository.findByExternalId(1)).thenReturn(Optional.of(AKU));
    sendUpdateEvent();
    verify(repository, atLeastOnce()).persist(any(PlayerEntity.class));
  }

  @Test
  void does_nothing_on_update_if_player_not_found() throws InterruptedException {
    sendUpdateEvent();
    verify(repository, never()).persist(any(PlayerEntity.class));
  }

  @Test
  void handles_delete_player_event() throws InterruptedException {
    when(repository.findByExternalId(1)).thenReturn(Optional.of(AKU));
    sendDeleteEvent();
    verify(repository, atLeastOnce()).delete(any(PlayerEntity.class));
  }

  @Test
  void does_nothing_on_delete_if_player_not_found() throws InterruptedException {
    sendDeleteEvent();
    verify(repository, never()).delete(any(PlayerEntity.class));
  }

  @Test
  void ignores_unknown_event_types() throws InterruptedException {
    sendEvent(new PlayerEvent(Action.UNKNOWN, null));
    verifyNoInteractions(repository);
  }

  private void sendEvent(PlayerEvent event) throws InterruptedException {
    var source = connector.source(IncomingChannels.PLAYER_STATE);
    source.send(event);
    // I guess in-memory test channels don't work so great with blocking code
    Thread.sleep(500);
  }

  private void sendUpdateEvent() throws InterruptedException {
    var payload = new PlayerPayload(1l, "Aku", "Ankka");
    sendEvent(new PlayerEvent(Action.UPDATE, payload));
  }

  private void sendDeleteEvent() throws InterruptedException {
    var payload = new PlayerPayload(1l, "Aku", "Ankka");
    sendEvent(new PlayerEvent(Action.DELETE, payload));
  }

}
