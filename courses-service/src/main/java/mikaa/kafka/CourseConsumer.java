package mikaa.kafka;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ApplicationScoped
class CourseConsumer {

  @Incoming("courses-in")
  void consume(CourseEvent event) {
    log.info("consumed event " + event);
  }

}
