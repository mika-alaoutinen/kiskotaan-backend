package mikaa.feature.course;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.config.IncomingChannels;
import mikaa.kiskotaan.domain.HoleEvent;
import mikaa.kiskotaan.domain.HolePayload;

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
        courseAdded(payload);
        break;
      case DELETE:
        courseDeleted(payload);
      case UPDATE:
        courseUpdated(payload);
      case UNKNOWN:
      default:
        log.warn("Unknown hole event type {}", event.getAction());
        break;
    }
  }

  private void courseAdded(HolePayload payload) {
    log.info("Hole added: {}", payload);
    service.add(payload);
  }

  private void courseDeleted(HolePayload payload) {
    log.info("Hole deleted: {}", payload);
    service.delete(payload);
  }

  private void courseUpdated(HolePayload payload) {
    log.info("Hole updated: {}", payload);
    // Do nothing
  }

}
