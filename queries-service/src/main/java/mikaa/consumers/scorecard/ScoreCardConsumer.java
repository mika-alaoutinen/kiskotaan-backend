package mikaa.consumers.scorecard;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.ScoreCardPayload;
import mikaa.config.IncomingChannels;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class ScoreCardConsumer {

  @Incoming(IncomingChannels.ScoreCard.SCORECARD_ADDED)
  @Transactional
  void scoreCardAdded(ScoreCardPayload payload) {
    log.info("received score card added event", payload);
  }

  @Incoming(IncomingChannels.ScoreCard.SCORECARD_DELETED)
  @Transactional
  void scoreCardDeleted(ScoreCardPayload payload) {
    log.info("received score card deleted event", payload);
  }

}
