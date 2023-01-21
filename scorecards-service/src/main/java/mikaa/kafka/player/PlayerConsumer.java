package mikaa.kafka.player;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
class PlayerConsumer {

  private static final Logger log = LoggerFactory.getLogger(PlayerConsumer.class);
  private final PlayerUpdater updater;

  @Incoming("players-in")
  void consumeCourses(PlayerEvent event) {
    var payload = event.payload();
    log.info("type " + event.type());
    log.info("payload " + payload);

    switch (event.type()) {
      case PLAYER_ADDED:
        updater.add(payload);
        break;
      case PLAYER_DELETED:
        updater.delete(payload);
        break;
      case PLAYER_UPDATED:
        updater.update(payload);
        break;
      default:
        log.warn("Unrecognized event type " + event.type());
        break;
    }
  }

}
