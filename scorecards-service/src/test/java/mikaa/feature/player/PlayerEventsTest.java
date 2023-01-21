package mikaa.feature.player;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.reactive.messaging.providers.connectors.InMemoryConnector;
import io.smallrye.reactive.messaging.providers.connectors.InMemorySource;
import mikaa.kafka.player.PlayerDTO;
import mikaa.kafka.player.PlayerEvent;
import mikaa.kafka.player.PlayerEventType;

@QuarkusTest
class PlayerEventsTest {

  private static final PlayerDTO MIKKI_HIIRI = new PlayerDTO(123, "Mikki", "Hiiri");

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

}
