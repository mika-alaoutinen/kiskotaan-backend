package mikaa.feature.course;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.config.IncomingChannels;
import mikaa.kiskotaan.courses.HoleEvent;
import mikaa.kiskotaan.courses.HolePayload;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class HoleConsumer {

  private final HoleService service;

  @Incoming(IncomingChannels.HOLE_STATE)
  @Transactional
  void holeEvent(HoleEvent event) {
    var payload = event.getPayload();

    switch (event.getAction()) {
      case ADD:
        holeAdded(payload);
        break;
      case DELETE:
        holeDeleted(payload);
        break;
      case UPDATE:
        holeUpdated(payload);
        break;
      case UNKNOWN:
      default:
        log.warn("Unknown hole event type {}", event.getAction());
        break;
    }
  }

  private void holeAdded(HolePayload payload) {
    log.info("Hole added: {}", payload);
    service.add(payload);
  }

  private void holeDeleted(HolePayload payload) {
    log.info("Hole deleted: {}", payload);
    service.delete(payload);
  }

  private void holeUpdated(HolePayload payload) {
    log.info("Hole updated: {}", payload);
    // Do nothing
  }

}
