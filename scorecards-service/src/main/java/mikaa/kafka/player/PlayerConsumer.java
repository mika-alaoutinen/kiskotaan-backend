package mikaa.kafka.player;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
class PlayerConsumer {

  private static final Logger log = LoggerFactory.getLogger(PlayerConsumer.class);

  @Incoming("players-in")
  void consumeCourses(PlayerEvent event) {
    log.info("type " + event.type());
    log.info("payload " + event.payload());
  }

}