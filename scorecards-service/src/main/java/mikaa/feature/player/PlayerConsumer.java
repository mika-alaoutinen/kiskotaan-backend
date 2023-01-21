package mikaa.feature.player;

import javax.enterprise.context.ApplicationScoped;

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
  void consumeCourses(PlayerEvent event) {
    var payload = event.payload();
    log.info("type " + event.type());
    log.info("payload " + payload);

    switch (event.type()) {
      case PLAYER_ADDED:
        service.add(payload);
        break;
      case PLAYER_DELETED:
        service.delete(payload);
        break;
      case PLAYER_UPDATED:
        service.update(payload);
        break;
      default:
        log.warn("Unrecognized event type " + event.type());
        break;
    }
  }

}