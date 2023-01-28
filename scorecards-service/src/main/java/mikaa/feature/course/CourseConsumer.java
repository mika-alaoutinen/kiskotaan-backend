package mikaa.feature.course;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

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
  @Transactional
  void consume(CourseEvent event) {
    var type = event.type();
    var payload = event.payload();

    log.info("type: %s, payload: %s".formatted(type, payload));

    switch (type) {
      case COURSE_ADDED -> service.add(payload);
      case COURSE_DELETED -> service.delete(payload);
      case COURSE_UPDATED -> service.update(payload);
      default -> log.warn("Unrecognized event type " + type);
    }
  }

}
