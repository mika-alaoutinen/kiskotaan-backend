package mikaa.feature.scorecards;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class ScoreCardsConsumer {

  @Incoming("scorecard-added")
  @Transactional
  void scoreCardAdded(String event) {
    log.info("received score card added event", event);
  }

  @Incoming("scorecard-deleted")
  @Transactional
  void scoreCardDeleted(String event) {
    log.info("received score card deleted event", event);
  }

  @Incoming("scorecard-updated")
  @Transactional
  void scoreCardUpdated(String event) {
    log.info("received score card updated event", event);
  }

}
