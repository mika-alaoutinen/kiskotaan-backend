package mikaa.feature.course;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.kiskotaan.domain.HolePayload;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class HoleConsumer {

  private final HoleService service;

  @Incoming("hole-added")
  @Transactional
  void courseAdded(HolePayload payload) {
    log.info("Hole added: {}", payload);
    service.add(payload);
  }

  @Incoming("hole-deleted")
  @Transactional
  void courseDeleted(HolePayload payload) {
    log.info("Hole deleted: {}", payload);
    service.delete(payload);
  }

  @Incoming("hole-updated")
  void courseUpdated(HolePayload payload) {
    log.info("Hole updated: {}", payload);
    // Do nothing
  }

}
