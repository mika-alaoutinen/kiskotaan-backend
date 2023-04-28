package mikaa.feature.course;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.events.hole.HolePayload;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class HoleConsumer {

  private final HoleService service;

  @Incoming("hole-added")
  @Transactional
  void courseAdded(HolePayload payload) {
    log.info("Hole added: %s".formatted(payload));
    service.add(payload);
  }

  @Incoming("hole-deleted")
  @Transactional
  void courseDeleted(HolePayload payload) {
    log.info("Hole deleted: %s".formatted(payload));
    service.delete(payload);
  }

  @Incoming("hole-updated")
  @Transactional
  void courseUpdated(HolePayload payload) {
    log.info("Hole updated: %s".formatted(payload));
    // Do nothing
  }

}
