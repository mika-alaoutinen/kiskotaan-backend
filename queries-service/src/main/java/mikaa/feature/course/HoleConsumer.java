package mikaa.feature.course;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.HolePayload;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class HoleConsumer {

  @Incoming("hole-added")
  @Transactional
  void holeAdded(HolePayload payload) {
    log.info("received hole added event", payload);
  }

  @Incoming("hole-deleted")
  @Transactional
  void holeDeleted(HolePayload payload) {
    log.info("received hole deleted event", payload);
  }

  @Incoming("hole-updated")
  @Transactional
  void holeUpdated(HolePayload payload) {
    log.info("received hole updated event", payload);
  }

}
