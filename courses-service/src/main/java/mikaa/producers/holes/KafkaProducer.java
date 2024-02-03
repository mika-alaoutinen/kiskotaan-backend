package mikaa.producers.holes;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.courses.HoleEvent;
import mikaa.kiskotaan.courses.HolePayload;
import mikaa.producers.OutgoingChannels;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import io.smallrye.reactive.messaging.kafka.Record;

@ApplicationScoped
class KafkaProducer implements HoleProducer {

  @Inject
  @Channel(OutgoingChannels.HOLE_STATE)
  Emitter<Record<Long, HoleEvent>> emitter;

  @Override
  public void holeAdded(HolePayload payload) {
    send(Action.ADD, payload);
  }

  @Override
  public void holeDeleted(HolePayload payload) {
    send(Action.DELETE, payload);
  }

  @Override
  public void holeUpdated(HolePayload payload) {
    send(Action.UPDATE, payload);
  }

  private void send(Action action, HolePayload payload) {
    var record = Record.of(payload.getId(), new HoleEvent(action, payload));
    emitter.send(record).toCompletableFuture().join();
  }

}
