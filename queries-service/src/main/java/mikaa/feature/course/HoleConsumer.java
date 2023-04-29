package mikaa.feature.course;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class HoleConsumer {

  @Incoming("hole-added")
  @Transactional
  void holeAdded(String event) {
    log.info("received hole added event", event);
  }

  @Incoming("hole-deleted")
  @Transactional
  void holeDeleted(String event) {
    log.info("received hole deleted event", event);
  }

  @Incoming("hole-updated")
  @Transactional
  void holeUpdated(String event) {
    log.info("received hole updated event", event);
  }

}
