package mikaa.producers.scorecard;

import java.util.Collections;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import jakarta.enterprise.context.ApplicationScoped;
import mikaa.config.IncomingChannels;
import mikaa.config.OutgoingChannels;
import mikaa.kiskotaan.domain.ScoreCardPayload;
import mikaa.kiskotaan.domain.ScoreCardStatePayload;

@ApplicationScoped
class ScoreCardStateProducer {

  @Incoming(IncomingChannels.ScoreCard.SCORECARD_ADDED)
  @Outgoing(OutgoingChannels.ScoreCard.SCORECARD_STATE)
  ScoreCardStatePayload sendNewScoreCardState(ScoreCardPayload payload) {
    return new ScoreCardStatePayload(
        payload.getId(),
        payload.getCourseId(),
        payload.getPlayerIds(),
        Collections.emptyMap());
  }

}
