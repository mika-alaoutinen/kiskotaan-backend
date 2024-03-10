package mikaa.feature.course;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.config.IncomingChannels;
import mikaa.domain.Hole;
import mikaa.kiskotaan.course.HoleEvent;
import mikaa.kiskotaan.course.HolePayload;
import mikaa.kiskotaan.domain.Action;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class HoleConsumer {

  private final HoleService service;

  @Incoming(IncomingChannels.HOLE_STATE)
  @Transactional
  void holeEvent(HoleEvent event) {
    var action = event.getAction();
    var payload = event.getPayload();

    switch (action) {
      case ADD -> holeAdded(payload);
      case DELETE -> holeDeleted(payload);
      case UPDATE -> holeUpdated(payload);
      case UNKNOWN -> handleUnknown(action);
      default -> handleUnknown(action);
    }
  }

  private void handleUnknown(Action action) {
    log.warn("Unknown hole event type {}", action);
  }

  private void holeAdded(HolePayload payload) {
    log.info("Hole added: {}", payload);
    service.add(payload.getCourseId(), from(payload));
  }

  private void holeDeleted(HolePayload payload) {
    log.info("Hole deleted: {}", payload);
    service.delete(payload.getCourseId(), from(payload));
  }

  private void holeUpdated(HolePayload payload) {
    log.info("Hole updated: {}", payload);
    // Do nothing
  }

  private static Hole from(HolePayload payload) {
    return new Hole(payload.getNumber(), payload.getPar());
  }

}
