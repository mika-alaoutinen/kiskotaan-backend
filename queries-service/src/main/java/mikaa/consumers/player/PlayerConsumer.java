package mikaa.consumers.player;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.PlayerPayload;
import mikaa.config.IncomingChannels;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class PlayerConsumer {

  private final PlayerWriter writer;

  @Incoming(IncomingChannels.Player.PLAYER_ADDED)
  Uni<Void> playerAdded(PlayerPayload payload) {
    log.info("received player added event: %s".formatted(payload));
    return writer.add(payload).replaceWithVoid();
  }

  @Incoming(IncomingChannels.Player.PLAYER_DELETED)
  Uni<Void> playerDeleted(PlayerPayload payload) {
    log.info("received player deleted event: %s".formatted(payload));
    return writer.delete(payload).replaceWithVoid();
  }

  @Incoming(IncomingChannels.Player.PLAYER_UPDATED)
  Uni<Void> playerUpdated(PlayerPayload payload) {
    log.info("received player updated event: %s".formatted(payload));
    return writer.update(payload).replaceWithVoid();
  }

}
