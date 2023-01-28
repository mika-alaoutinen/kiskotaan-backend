package mikaa.feature.course;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;
import mikaa.events.course.CourseEvent;

@ApplicationScoped
@RequiredArgsConstructor
class CourseConsumer {

  private static final Logger log = LoggerFactory.getLogger(CourseConsumer.class);
  private final CourseService service;

  @Incoming("courses-in")
  void consume(CourseEvent event) {
    var type = event.type();
    var payload = event.payload();

    log.info("type " + type);
    log.info("payload " + payload);

    switch (type) {
      case COURSE_ADDED:
        break;
      case COURSE_DELETED:
        break;
      case COURSE_UPDATED:
        break;
      default:
        log.warn("Unrecognized event type " + type);
        break;
    }
  }

}
