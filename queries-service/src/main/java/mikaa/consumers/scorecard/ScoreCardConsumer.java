package mikaa.consumers.scorecard;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.config.IncomingChannels;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.domain.ScoreCardEvent;
import mikaa.kiskotaan.domain.ScoreCardPayload;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class ScoreCardConsumer {

  private final ScoreCardWriter writer;

  @Incoming(IncomingChannels.SCORECARD_STATE)
  Uni<Void> consumeEvent(ScoreCardEvent event) {
    var payload = event.getPayload();

    return switch (event.getAction()) {
      case ADD -> scoreCardAdded(payload);
      case UPDATE -> scoreCardUpdated(payload);
      case DELETE -> scoreCardDeleted(payload);
      case UNKNOWN -> handleUnknown(event.getAction());
    };
  }

  Uni<Void> scoreCardAdded(ScoreCardPayload payload) {
    log.info("received score card added event: {}", payload);
    return writer.add(payload).replaceWithVoid();
  }

  Uni<Void> scoreCardUpdated(ScoreCardPayload payload) {
    log.info("received score card update event: {}", payload);
    return writer.update(payload).replaceWithVoid();
  }

  Uni<Void> scoreCardDeleted(ScoreCardPayload payload) {
    log.info("received score card deleted event: {}", payload);
    return writer.delete(payload).replaceWithVoid();
  }

  private Uni<Void> handleUnknown(Action action) {
    log.warn("Unknown score card event type {}", action);
    return Uni.createFrom().voidItem();
  }

}
