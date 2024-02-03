package mikaa.consumers.course;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.config.IncomingChannels;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.courses.HoleEvent;
import mikaa.kiskotaan.courses.HolePayload;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class HoleConsumer {

  private final HoleWriter writer;

  @Incoming(IncomingChannels.HOLE_STATE)
  Uni<Void> consumeEvent(HoleEvent event) {
    var payload = event.getPayload();

    return switch (event.getAction()) {
      case ADD -> holeAdded(payload);
      case UPDATE -> holeUpdated(payload);
      case DELETE -> holeDeleted(payload);
      case UNKNOWN -> handleUnknown(event.getAction());
    };
  }

  private Uni<Void> holeAdded(HolePayload payload) {
    log.info("received hole added event: {}", payload);
    return writer.add(payload).replaceWithVoid();
  }

  private Uni<Void> holeDeleted(HolePayload payload) {
    log.info("received hole deleted event: {}", payload);
    return writer.delete(payload).replaceWithVoid();
  }

  private Uni<Void> holeUpdated(HolePayload payload) {
    log.info("received hole updated event: {}", payload);
    return writer.update(payload).replaceWithVoid();
  }

  private Uni<Void> handleUnknown(Action action) {
    log.warn("Unknown hole event type {}", action);
    return Uni.createFrom().voidItem();
  }

}
