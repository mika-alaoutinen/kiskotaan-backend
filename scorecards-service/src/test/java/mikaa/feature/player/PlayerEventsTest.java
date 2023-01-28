package mikaa.feature.player;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.reactive.messaging.providers.connectors.InMemoryConnector;
import io.smallrye.reactive.messaging.providers.connectors.InMemorySource;
import mikaa.events.player.PlayerPayload;
import mikaa.events.player.PlayerEvent;
import mikaa.events.player.PlayerEventType;

@QuarkusTest
class PlayerEventsTest {

  private static final PlayerPayload MIKKI_HIIRI = new PlayerPayload(123, "Mikki", "Hiiri");

  @Any
  @Inject
  private InMemoryConnector connector;

  @InjectMock
  private PlayerRepository repository;

  private InMemorySource<PlayerEvent> source;

  @BeforeEach
  void setup() {
    source = connector.source("players-in");
  }

  @Test
  void should_save_new_player() {
    source.send(new PlayerEvent(PlayerEventType.PLAYER_ADDED, MIKKI_HIIRI));
    verify(repository, atLeastOnce()).persist(any(PlayerEntity.class));
  }

  @Test
  void should_update_player() {
    var player = new PlayerEntity(123L, "Pekka", "Kana");
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(player));

    source.send(new PlayerEvent(PlayerEventType.PLAYER_UPDATED, MIKKI_HIIRI));
    verify(repository, atLeastOnce()).persist(any(PlayerEntity.class));
  }

  @Test
  void should_do_nothing_on_update_if_player_not_found() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.empty());

    source.send(new PlayerEvent(PlayerEventType.PLAYER_UPDATED, MIKKI_HIIRI));
    verify(repository, never()).persist(any(PlayerEntity.class));
  }

  @Test
  void should_delete_player() {
    source.send(new PlayerEvent(PlayerEventType.PLAYER_DELETED, MIKKI_HIIRI));
    verify(repository, atLeastOnce()).deleteById(123L);
  }

}
