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

  @Incoming("players-in")
  @Transactional
  void consume(String event) {
    log.info("received player event", event);
  }

}
