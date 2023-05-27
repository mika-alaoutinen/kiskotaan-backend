package mikaa.consumers.scorecard;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.config.IncomingChannels;
import mikaa.kiskotaan.domain.ScorePayload;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class ScoreConsumer {

  @Incoming(IncomingChannels.Score.SCORE_ADDED)
  @Transactional
  void scoreAdded(ScorePayload payload) {
    log.info("received score added event", payload);
  }

  @Incoming(IncomingChannels.Score.SCORE_DELETED)
  @Transactional
  void scoreDeleted(ScorePayload payload) {
    log.info("received score deleted event", payload);
  }

}
