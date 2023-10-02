package mikaa.producers.courses;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.domain.CourseEvent;
import mikaa.kiskotaan.domain.CoursePayload;
import mikaa.producers.OutgoingChannels;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import io.smallrye.reactive.messaging.kafka.Record;

@ApplicationScoped
class KafkaProducer implements CourseProducer {

  @Inject
  @Channel(OutgoingChannels.COURSE_STATE)
  Emitter<Record<Long, CourseEvent>> emitter;

  @Override
  public void courseAdded(CoursePayload payload) {
    send(Action.ADD, payload);
  }

  @Override
  public void courseUpdated(CoursePayload payload) {
    send(Action.UPDATE, payload);
  }

  @Override
  public void courseDeleted(CoursePayload payload) {
    send(Action.DELETE, payload);
  }

  private void send(Action action, CoursePayload payload) {
    var record = Record.of(payload.getId(), new CourseEvent(action, payload));
    emitter.send(record).toCompletableFuture().join();
  }

}
