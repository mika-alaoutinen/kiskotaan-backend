package mikaa.kafka.courses;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import mikaa.dto.CourseDTO;
import mikaa.dto.CourseNameDTO;

@ApplicationScoped
public class CourseProducer {

  @Inject
  @Channel("courses-out")
  Emitter<CourseEvent> courseEmitter;

  @Inject
  @Channel("courses-out")
  Emitter<CourseNameEvent> courseNameEmitter;

  public void send(CourseEventType type, CourseDTO course) {
    var acked = switch (type) {
      case COURSE_ADDED -> courseEmitter.send(new CourseEvent(type, course));

      case COURSE_DELETED, COURSE_UPDATED -> {
        var courseName = new CourseNameDTO(course.id(), course.name());
        yield courseNameEmitter.send(new CourseNameEvent(type, courseName));
      }
    };

    acked.toCompletableFuture().join();
  }

}
