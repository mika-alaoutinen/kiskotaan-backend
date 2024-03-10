package mikaa.feature.course;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.kiskotaan.course.CourseEvent;
import mikaa.kiskotaan.course.CoursePayload;
import mikaa.kiskotaan.domain.Action;
import mikaa.config.IncomingChannels;
import mikaa.domain.Course;
import mikaa.domain.Hole;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class CourseConsumer {

  private final CourseService service;

  @Incoming(IncomingChannels.COURSE_STATE)
  @Transactional
  void courseEvent(CourseEvent event) {
    var action = event.getAction();
    var payload = event.getPayload();

    switch (action) {
      case ADD -> courseAdded(payload);
      case UPDATE -> courseUpdated(payload);
      case DELETE -> courseDeleted(payload);
      case UNKNOWN -> handleUnknownEvent(action);
      default -> handleUnknownEvent(action);
    }
  }

  private void handleUnknownEvent(Action action) {
    log.warn("Unknown course event type {}", action);
  }

  private void courseAdded(CoursePayload payload) {
    log.info("Course added: {}", payload);
    service.add(from(payload));
  }

  private void courseDeleted(CoursePayload payload) {
    log.info("Course deleted: {}", payload);
    service.delete(from(payload));
  }

  private void courseUpdated(CoursePayload payload) {
    log.info("Course updated: {}", payload);
    service.update(from(payload));
  }

  private static Course from(CoursePayload payload) {
    var holes = payload.getHoles()
        .stream()
        .map(h -> new Hole(h.getNumber(), h.getPar()))
        .toList();

    return new Course(payload.getId(), payload.getName(), holes);
  }

}
