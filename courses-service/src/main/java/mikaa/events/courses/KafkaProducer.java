package mikaa.events.courses;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
class KafkaProducer implements CourseProducer {

  @Inject
  @Channel("course-added")
  Emitter<CoursePayload> addEmitter;

  @Inject
  @Channel("course-deleted")
  Emitter<Long> deleteEmitter;

  @Inject
  @Channel("course-updated")
  Emitter<CoursePayload> updateEmitter;

  @Override
  public void courseAdded(CoursePayload payload) {
    addEmitter.send(payload);
  }

  @Override
  public void courseUpdated(CoursePayload payload) {
    updateEmitter.send(payload);
  }

  @Override
  public void courseDeleted(CoursePayload payload) {
    deleteEmitter.send(payload.id());
  }

}
