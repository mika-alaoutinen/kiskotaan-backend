package mikaa.feature.course;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mikaa.events.course.CourseEvent;

@ApplicationScoped
class CourseConsumer {

  private static final Logger log = LoggerFactory.getLogger(CourseConsumer.class);

  @Incoming("courses-in")
  void consumeCourses(CourseEvent event) {
    log.info("type " + event.type());
    log.info("payload " + event.payload());
  }

}
