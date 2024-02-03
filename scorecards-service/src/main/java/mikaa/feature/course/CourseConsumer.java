package mikaa.feature.course;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.kiskotaan.courses.CourseEvent;
import mikaa.kiskotaan.courses.CoursePayload;
import mikaa.config.IncomingChannels;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class CourseConsumer {

  private final CourseService service;

  @Incoming(IncomingChannels.COURSE_STATE)
  @Transactional
  void courseEvent(CourseEvent event) {
    var payload = event.getPayload();

    switch (event.getAction()) {
      case ADD:
        courseAdded(payload);
        break;
      case UPDATE:
        courseUpdated(payload);
        break;
      case DELETE:
        courseDeleted(payload);
        break;
      case UNKNOWN:
      default:
        log.warn("Unknown course event type {}", event.getAction());
        break;
    }
  }

  private void courseAdded(CoursePayload payload) {
    log.info("Course added: {}", payload);
    service.add(payload);
  }

  private void courseDeleted(CoursePayload payload) {
    log.info("Course deleted: {}", payload);
    service.delete(payload);
  }

  private void courseUpdated(CoursePayload payload) {
    log.info("Course updated: {}", payload);
    service.update(payload);
  }

}
