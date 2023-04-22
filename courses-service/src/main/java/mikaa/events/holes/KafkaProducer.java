package mikaa.events.holes;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
class KafkaProducer implements HoleProducer {

  @Inject
  @Channel("hole-added")
  Emitter<HolePayload> addEmitter;

  @Inject
  @Channel("hole-updated")
  Emitter<HolePayload> updateEmitter;

  @Inject
  @Channel("hole-deleted")
  Emitter<Long> deleteEmitter;

  @Override
  public void holeAdded(HolePayload payload) {
    addEmitter.send(payload);
  }

  @Override
  public void holeUpdated(HolePayload payload) {
    updateEmitter.send(payload);
  }

  @Override
  public void holeDeleted(long id) {
    deleteEmitter.send(id);
  }

}
