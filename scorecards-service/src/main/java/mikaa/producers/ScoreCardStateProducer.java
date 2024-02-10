package mikaa.producers;

import io.smallrye.reactive.messaging.kafka.Record;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import jakarta.enterprise.context.ApplicationScoped;
import mikaa.config.OutgoingChannels;
import mikaa.kiskotaan.scorecard.ScoreCardEvent;

@ApplicationScoped
class ScoreCardStateProducer {

  @Incoming(ScoreCardProducer.INTERNAL_SCORECARD_CHANNEL)
  @Outgoing(OutgoingChannels.SCORECARD_STATE)
  Record<Long, ScoreCardEvent> scoreCardAdded(ScoreCardEvent event) {
    return sendRecord(event);
  }

  Record<Long, ScoreCardEvent> scoreCardDeleted(ScoreCardEvent event) {
    return sendRecord(event);
  }

  Record<Long, ScoreCardEvent> scoreCardUpdated(ScoreCardEvent event) {
    return sendRecord(event);
  }

  private static Record<Long, ScoreCardEvent> sendRecord(ScoreCardEvent event) {
    return Record.of(event.getPayload().getId(), event);
  }

}
