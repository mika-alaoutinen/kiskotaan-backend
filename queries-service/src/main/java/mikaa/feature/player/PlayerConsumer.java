package mikaa.feature.player;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.events.IncomingChannels;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class PlayerConsumer {

  @Incoming(IncomingChannels.Player.PLAYER_ADDED)
  @Transactional
  void playerAdded(String event) {
    log.info("received player added event", event);
  }

  @Incoming(IncomingChannels.Player.PLAYER_DELETED)
  @Transactional
  void playerDeleted(String event) {
    log.info("received player deleted event", event);
  }

  @Incoming(IncomingChannels.Player.PLAYER_UPDATED)
  @Transactional
  void playerUpdated(String event) {
    log.info("received player updated event", event);
  }

}
