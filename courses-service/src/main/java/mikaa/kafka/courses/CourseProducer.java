package mikaa.kafka.courses;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class CourseProducer {

  @Inject
  @Channel("courses-out")
  Emitter<CourseEvent> emitter;

  public void send(CourseEventType type, CoursePayload payload) {
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
