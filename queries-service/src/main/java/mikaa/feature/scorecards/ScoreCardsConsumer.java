package mikaa.feature.scorecards;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.events.IncomingChannels;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class ScoreCardsConsumer {

  @Incoming(IncomingChannels.ScoreCard.SCORECARD_ADDED)
  @Transactional
  void scoreCardAdded(String event) {
    log.info("received score card added event", event);
  }

  @Incoming(IncomingChannels.ScoreCard.SCORECARD_DELETED)
  @Transactional
  void scoreCardDeleted(String event) {
    log.info("received score card deleted event", event);
  }

}
