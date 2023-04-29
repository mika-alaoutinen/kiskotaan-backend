package mikaa.feature.scorecards;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class ScoreConsumer {

  @Incoming("score-added")
  @Transactional
  void scoreAdded(String event) {
    log.info("received score added event", event);
  }

  @Incoming("score-deleted")
  @Transactional
  void scoreDeleted(String event) {
    log.info("received score deleted event", event);
  }

}
