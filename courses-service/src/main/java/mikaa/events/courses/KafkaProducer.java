package mikaa.events.courses;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
class KafkaProducer implements CourseProducer {

  @Inject
  @Channel("courses-out")
  Emitter<CourseEvent> emitter;

  @Override
  public void courseAdded(CoursePayload course) {
    send(new CourseEvent(CourseEventType.COURSE_ADDED, course));
  }

  @Override
  public void courseUpdated(CoursePayload course) {
    var payload = withoutHoles(course);
    send(new CourseEvent(CourseEventType.COURSE_UPDATED, payload));
  }

  @Override
  public void courseDeleted(CoursePayload course) {
    var payload = withoutHoles(course);
    send(new CourseEvent(CourseEventType.COURSE_DELETED, payload));
  }

  private void send(CourseEvent event) {
    emitter.send(event).toCompletableFuture().join();
  }

  private static CoursePayload withoutHoles(CoursePayload payload) {
    return new CoursePayload(payload.id(), payload.name(), List.of());
  }

}
