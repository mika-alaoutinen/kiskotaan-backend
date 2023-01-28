package mikaa.feature.player;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;
import mikaa.events.player.PlayerEvent;

@ApplicationScoped
@RequiredArgsConstructor
class PlayerConsumer {

  private static final Logger log = LoggerFactory.getLogger(PlayerConsumer.class);
  private final PlayerService service;

  @Incoming("players-in")
  @Transactional
  void consume(PlayerEvent event) {
    var type = event.type();
    var payload = event.payload();

    log.info("type " + type);
    log.info("payload " + payload);

    switch (type) {
      case PLAYER_ADDED -> service.add(payload);
      case PLAYER_DELETED -> service.delete(payload);
      case PLAYER_UPDATED -> service.update(payload);
      default -> log.warn("Unrecognized event type " + type);
    }
  }

}
