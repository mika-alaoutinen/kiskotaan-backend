package mikaa.consumers.player;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.PlayerPayload;
import mikaa.config.IncomingChannels;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class PlayerConsumer {

  @Incoming(IncomingChannels.Player.PLAYER_ADDED)
  @Transactional
  void playerAdded(PlayerPayload payload) {
    log.info("received player added event", payload);
  }

  @Incoming(IncomingChannels.Player.PLAYER_DELETED)
  @Transactional
  void playerDeleted(PlayerPayload payload) {
    log.info("received player deleted event", payload);
  }

  @Incoming(IncomingChannels.Player.PLAYER_UPDATED)
  @Transactional
  void playerUpdated(PlayerPayload payload) {
    log.info("received player updated event", payload);
  }

}
