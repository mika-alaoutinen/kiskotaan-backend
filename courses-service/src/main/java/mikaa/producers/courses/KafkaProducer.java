package mikaa.producers.courses;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mikaa.kiskotaan.courses.CoursePayload;
import mikaa.kiskotaan.courses.CourseUpdated;
import mikaa.producers.OutgoingChannels;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
class KafkaProducer implements CourseProducer {

  @Inject
  @Channel(OutgoingChannels.Course.COURSE_ADDED)
  Emitter<CoursePayload> addEmitter;

  @Inject
  @Channel(OutgoingChannels.Course.COURSE_DELETED)
  Emitter<CoursePayload> deleteEmitter;

  @Inject
  @Channel(OutgoingChannels.Course.COURSE_UPDATED)
  Emitter<CourseUpdated> updateEmitter;

  @Override
  public void courseAdded(CoursePayload payload) {
    addEmitter.send(payload).toCompletableFuture().join();
  }

  @Override
  public void courseUpdated(CourseUpdated payload) {
    updateEmitter.send(payload).toCompletableFuture().join();
  }

  @Override
  public void courseDeleted(CoursePayload payload) {
    deleteEmitter.send(payload).toCompletableFuture().join();
  }

}
