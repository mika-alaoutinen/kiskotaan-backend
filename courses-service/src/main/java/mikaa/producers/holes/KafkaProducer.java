package mikaa.producers.holes;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mikaa.kiskotaan.domain.HolePayload;
import mikaa.producers.OutgoingChannels;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import io.smallrye.reactive.messaging.kafka.Record;

@ApplicationScoped
class KafkaProducer implements HoleProducer {

  @Inject
  @Channel(OutgoingChannels.Hole.HOLE_ADDED)
  Emitter<Record<Long, HolePayload>> addEmitter;

  @Inject
  @Channel(OutgoingChannels.Hole.HOLE_DELETED)
  Emitter<Record<Long, HolePayload>> deleteEmitter;

  @Inject
  @Channel(OutgoingChannels.Hole.HOLE_UPDATED)
  Emitter<Record<Long, HolePayload>> updateEmitter;

  @Override
  public void holeAdded(HolePayload payload) {
    var record = toRecord(payload);
    addEmitter.send(record).toCompletableFuture().join();
  }

  @Override
  public void holeDeleted(HolePayload payload) {
    var record = toRecord(payload);
    deleteEmitter.send(record).toCompletableFuture().join();
  }

  @Override
  public void holeUpdated(HolePayload payload) {
    var record = toRecord(payload);
    updateEmitter.send(record).toCompletableFuture().join();
  }

  private static Record<Long, HolePayload> toRecord(HolePayload payload) {
    return Record.of(payload.getId(), payload);
  }

}
