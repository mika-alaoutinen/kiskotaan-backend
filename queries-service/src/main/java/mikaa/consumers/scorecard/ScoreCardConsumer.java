package mikaa.consumers.scorecard;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.config.IncomingChannels;
import mikaa.kiskotaan.domain.ScoreCardPayload;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class ScoreCardConsumer {

  private final ScoreCardWriter writer;

  @Incoming(IncomingChannels.ScoreCard.SCORECARD_ADDED)
  Uni<Void> scoreCardAdded(ScoreCardPayload payload) {
    log.info("received score card added event: {}", payload);
    return writer.add(payload).replaceWithVoid();
  }

  @Incoming(IncomingChannels.ScoreCard.SCORECARD_DELETED)
  Uni<Void> scoreCardDeleted(ScoreCardPayload payload) {
    log.info("received score card deleted event: {}", payload);
    return writer.delete(payload).replaceWithVoid();
  }

}
