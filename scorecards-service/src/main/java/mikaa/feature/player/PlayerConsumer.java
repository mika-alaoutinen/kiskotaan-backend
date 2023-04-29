package mikaa.feature.player;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.events.player.PlayerPayload;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class PlayerConsumer {

  private final PlayerService service;

  @Incoming("player-added")
  @Transactional
  void playerAdded(PlayerPayload payload) {
    log.info("Player added %s".formatted(payload));
    service.add(payload);
  }

  @Incoming("player-deleted")
  @Transactional
  void playerDeleted(PlayerPayload payload) {
    log.info("Player deleted %s".formatted(payload));
    service.delete(payload);
  }

  @Incoming("player-updated")
  @Transactional
  void playerUpdated(PlayerPayload payload) {
    log.info("Player updated %s".formatted(payload));
    service.update(payload);
  }

}
