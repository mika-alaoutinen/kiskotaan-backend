package mikaa.feature.scorecards;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.config.IncomingChannels;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class ScoreConsumer {

  @Incoming(IncomingChannels.Score.SCORE_ADDED)
  @Transactional
  void scoreAdded(String event) {
    log.info("received score added event", event);
  }

  @Incoming(IncomingChannels.Score.SCORE_DELETED)
  @Transactional
  void scoreDeleted(String event) {
    log.info("received score deleted event", event);
  }

}
