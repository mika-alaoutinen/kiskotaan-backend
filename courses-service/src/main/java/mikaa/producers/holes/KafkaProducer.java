package mikaa.producers.holes;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mikaa.kiskotaan.courses.HolePayload;
import mikaa.producers.OutgoingChannels;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
class KafkaProducer implements HoleProducer {

  @Inject
  @Channel(OutgoingChannels.Hole.HOLE_ADDED)
  Emitter<HolePayload> addEmitter;

  @Inject
  @Channel(OutgoingChannels.Hole.HOLE_DELETED)
  Emitter<HolePayload> deleteEmitter;

  @Inject
  @Channel(OutgoingChannels.Hole.HOLE_UPDATED)
  Emitter<HolePayload> updateEmitter;

  @Override
  public void holeAdded(HolePayload payload) {
    addEmitter.send(payload).toCompletableFuture().join();
  }

  @Override
  public void holeDeleted(HolePayload payload) {
    deleteEmitter.send(payload).toCompletableFuture().join();
  }

  @Override
  public void holeUpdated(HolePayload payload) {
    updateEmitter.send(payload).toCompletableFuture().join();
  }

}
