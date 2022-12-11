package mikaa.kafka;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
class CourseConsumer {

  private static final Logger log = LoggerFactory.getLogger(CourseConsumer.class);

  @Incoming("courses-in")
  void consumeCourses(String event) {
    log.info(event);
  }

}
