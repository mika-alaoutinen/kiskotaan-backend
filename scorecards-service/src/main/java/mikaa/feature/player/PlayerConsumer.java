package mikaa.feature.player;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.player.PlayerEvent;
import mikaa.kiskotaan.player.PlayerPayload;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class PlayerConsumer {

  static final String PLAYER_STATE = "player-state";

  private final PlayerService service;

  @Incoming(PLAYER_STATE)
  @Transactional
  void playerEvent(PlayerEvent event) {
    var action = event.getAction();
    var payload = event.getPayload();

    switch (action) {
      case ADD -> playerAdded(payload);
      case UPDATE -> playerUpdated(payload);
      case DELETE -> playerDeleted(payload);
      case UNKNOWN -> handleUnknownEvent(action);
      default -> handleUnknownEvent(action);
    }
  }

  private void handleUnknownEvent(Action action) {
    log.warn("Unknown player event type {}", action);
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
