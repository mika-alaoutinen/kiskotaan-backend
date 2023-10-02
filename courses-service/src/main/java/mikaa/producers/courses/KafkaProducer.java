package mikaa.producers.courses;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mikaa.kiskotaan.domain.CoursePayload;
import mikaa.producers.OutgoingChannels;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import io.smallrye.reactive.messaging.kafka.Record;

@ApplicationScoped
class KafkaProducer implements CourseProducer {

  @Inject
  @Channel(OutgoingChannels.Course.COURSE_ADDED)
  Emitter<Record<Long, CoursePayload>> addEmitter;

  @Inject
  @Channel(OutgoingChannels.Course.COURSE_DELETED)
  Emitter<Record<Long, CoursePayload>> deleteEmitter;

  @Inject
  @Channel(OutgoingChannels.Course.COURSE_UPDATED)
  Emitter<Record<Long, CoursePayload>> updateEmitter;

  @Override
  public void courseAdded(CoursePayload payload) {
    var record = Record.of(payload.getId(), payload);
    addEmitter.send(record).toCompletableFuture().join();
  }

  @Override
  public void courseUpdated(CoursePayload payload) {
    var record = Record.of(payload.getId(), payload);
    updateEmitter.send(record).toCompletableFuture().join();
  }

  @Override
  public void courseDeleted(CoursePayload payload) {
    var record = Record.of(payload.getId(), payload);
    deleteEmitter.send(record).toCompletableFuture().join();
  }

}
