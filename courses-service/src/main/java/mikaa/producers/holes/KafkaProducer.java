package mikaa.producers.holes;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mikaa.kiskotaan.domain.Action;
import mikaa.domain.Hole;
import mikaa.kiskotaan.course.HoleEvent;
import mikaa.kiskotaan.course.HolePayload;
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
  public void holeAdded(Hole hole, long courseId) {
    send(Action.ADD, hole, courseId);
  }

  @Override
  public void holeDeleted(Hole hole, long courseId) {
    send(Action.DELETE, hole, courseId);
  }

  @Override
  public void holeUpdated(Hole hole, long courseId) {
    send(Action.UPDATE, hole, courseId);
  }

  private void send(Action action, Hole hole, long courseId) {
    var payload = new HolePayload(hole.id(), courseId, hole.number(), hole.par(), hole.distance());
    var record = Record.of(payload.getId(), new HoleEvent(action, payload));
    emitter.send(record).toCompletableFuture().join();
  }

}
