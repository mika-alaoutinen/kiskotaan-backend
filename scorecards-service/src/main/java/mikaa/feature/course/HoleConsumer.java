package mikaa.feature.course;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;
import mikaa.events.hole.HoleEvent;

@ApplicationScoped
@RequiredArgsConstructor
class HoleConsumer {

  private static final Logger log = LoggerFactory.getLogger(HoleConsumer.class);
  private final HoleService service;

  @Incoming("holes-in")
  // @Transactional
  void consume(HoleEvent event) {
    var type = event.type();
    var payload = event.payload();

    log.info("type: %s, payload: %s".formatted(type, payload));

    switch (type) {
      case HOLE_ADDED -> service.add(payload);
      case HOLE_DELETED -> service.delete(payload);
      case HOLE_UPDATED -> {
        // do nothing
      }
      default -> log.warn("Unrecognized event type " + type);
    }
  }

}
