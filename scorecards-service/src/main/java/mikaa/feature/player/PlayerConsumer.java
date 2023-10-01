package mikaa.feature.player;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.config.IncomingChannels;
import mikaa.kiskotaan.domain.PlayerEvent;
import mikaa.kiskotaan.domain.PlayerPayload;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class PlayerConsumer {

  private final PlayerService service;

  @Incoming(IncomingChannels.PLAYER_STATE)
  @Transactional
  void playerEvent(PlayerEvent event) {
    var payload = event.getPayload();

    switch (event.getAction()) {
      case ADD:
        playerAdded(payload);
        break;
      case UPDATE:
        playerUpdated(payload);
        break;
      case DELETE:
        playerDeleted(payload);
        break;
      case UNKNOWN:
      default:
        log.warn("Unknown player event type {}", event.getAction());
        break;
    }
  }

  private void playerAdded(PlayerPayload payload) {
    log.info("Player added {}", payload);
    service.add(payload);
  }

  private void playerDeleted(PlayerPayload payload) {
    log.info("Player deleted {}", payload);
    service.delete(payload);
  }

  private void playerUpdated(PlayerPayload payload) {
    log.info("Player updated {}", payload);
    service.update(payload);
  }

}
