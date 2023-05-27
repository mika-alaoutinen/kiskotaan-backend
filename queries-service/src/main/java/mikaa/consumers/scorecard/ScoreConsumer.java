package mikaa.consumers.scorecard;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.config.IncomingChannels;
import mikaa.kiskotaan.domain.ScorePayload;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class ScoreConsumer {

  private final ScoreWriter writer;

  @Incoming(IncomingChannels.Score.SCORE_ADDED)
  Uni<Void> scoreAdded(ScorePayload payload) {
    log.info("received score added event", payload);
    return writer.add(payload).replaceWithVoid();
  }

  @Incoming(IncomingChannels.Score.SCORE_DELETED)
  Uni<Void> scoreDeleted(ScorePayload payload) {
    log.info("received score deleted event", payload);
    return writer.delete(payload).replaceWithVoid();
  }

}
