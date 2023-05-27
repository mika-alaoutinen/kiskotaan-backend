package mikaa.consumers.course;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.kiskotaan.domain.HolePayload;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class HoleConsumer {

  private final HoleWriter writer;

  @Incoming("hole-added")
  Uni<Void> holeAdded(HolePayload payload) {
    log.info("received hole added event: {}", payload);
    return writer.add(payload).replaceWithVoid();
  }

  @Incoming("hole-deleted")
  Uni<Void> holeDeleted(HolePayload payload) {
    log.info("received hole deleted event: {}", payload);
    return writer.delete(payload).replaceWithVoid();
  }

  @Incoming("hole-updated")
  Uni<Void> holeUpdated(HolePayload payload) {
    log.info("received hole updated event: {}", payload);
    return writer.update(payload).replaceWithVoid();
  }

}
