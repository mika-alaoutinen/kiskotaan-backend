package mikaa.consumers.player;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.config.IncomingChannels;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.player.PlayerEvent;
import mikaa.kiskotaan.player.PlayerPayload;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class PlayerConsumer {

  private final PlayerWriter writer;

  @Incoming(IncomingChannels.PLAYER_STATE)
  Uni<Void> consumeEvent(PlayerEvent event) {
    var payload = event.getPayload();

    return switch (event.getAction()) {
      case ADD -> playerAdded(payload);
      case UPDATE -> playerUpdated(payload);
      case DELETE -> playerDeleted(payload);
      case UNKNOWN -> handleUnknown(event.getAction());
    };
  }

  private Uni<Void> playerAdded(PlayerPayload payload) {
    log.info("received player added event: {}", payload);
    return writer.add(payload).replaceWithVoid();
  }

  private Uni<Void> playerDeleted(PlayerPayload payload) {
    log.info("received player deleted event: {}", payload);
    return writer.delete(payload).replaceWithVoid();
  }

  private Uni<Void> playerUpdated(PlayerPayload payload) {
    log.info("received player updated event: {}", payload);
    return writer.update(payload).replaceWithVoid();
  }

  private Uni<Void> handleUnknown(Action action) {
    log.warn("Unknown player event type {}", action);
    return Uni.createFrom().voidItem();
  }

}
