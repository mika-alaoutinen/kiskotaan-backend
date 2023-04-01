package mikaa.kafka.courses;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import mikaa.dto.CourseDTO;

@ApplicationScoped
public class CourseProducer {

  @Inject
  @Channel("courses-out")
  Emitter<CourseEvent> emitter;

  public void send(CourseEventType type, CourseDTO course) {
    var acked = switch (type) {
      case COURSE_ADDED -> emitter.send(new CourseEvent(type, course));

      case COURSE_DELETED, COURSE_UPDATED -> {
        var courseWithoutHoles = new CourseDTO(course.id(), course.name(), List.of());
        yield emitter.send(new CourseEvent(type, courseWithoutHoles));
      }
    };

    acked.toCompletableFuture().join();
  }

}
