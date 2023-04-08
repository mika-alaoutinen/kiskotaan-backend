package mikaa.kafka.courses;

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
    send(CourseEventType.COURSE_ADDED, course);
  }

  @Override
  public void courseUpdated(CoursePayload course) {
    send(CourseEventType.COURSE_UPDATED, course);
  }

  @Override
  public void courseDeleted(CoursePayload course) {
    send(CourseEventType.COURSE_DELETED, course);
  }

  private void send(CourseEventType type, CoursePayload payload) {
    var acked = switch (type) {
      case COURSE_ADDED -> emitter.send(new CourseEvent(type, payload));

      case COURSE_DELETED, COURSE_UPDATED -> {
        var courseWithoutHoles = new CoursePayload(payload.id(), payload.name(), List.of());
        yield emitter.send(new CourseEvent(type, courseWithoutHoles));
      }
    };

    acked.toCompletableFuture().join();
  }

}
