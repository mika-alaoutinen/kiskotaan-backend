package mikaa.feature.player;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class PlayerConsumer {

  @Incoming("player-added")
  @Transactional
  void playerAdded(String event) {
    log.info("received player added event", event);
  }

  @Incoming("player-deleted")
  @Transactional
  void playerDeleted(String event) {
    log.info("received player deleted event", event);
  }

  @Incoming("player-updated")
  @Transactional
  void playerUpdated(String event) {
    log.info("received player updated event", event);
  }

}
