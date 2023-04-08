package mikaa.kafka.holes;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
class KafkaProducer implements HoleProducer {

  @Inject
  @Channel("holes-out")
  Emitter<HoleEvent> emitter;

  @Override
  public void holeAdded(HolePayload payload) {
    send(HoleEventType.HOLE_ADDED, payload);
  }

  @Override
  public void holeUpdated(HolePayload payload) {
    send(HoleEventType.HOLE_UPDATED, payload);
  }

  @Override
  public void holeDeleted(HolePayload payload) {
    send(HoleEventType.HOLE_DELETED, payload);
  }

  private void send(HoleEventType type, HolePayload payload) {
    var acked = emitter.send(new HoleEvent(type, payload));
    acked.toCompletableFuture().join();
  }

}
