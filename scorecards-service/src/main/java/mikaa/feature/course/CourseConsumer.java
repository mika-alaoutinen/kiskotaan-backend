package mikaa.feature.course;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.CoursePayload;
import mikaa.CourseUpdated;
import mikaa.events.IncomingChannels;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class CourseConsumer {

  private final CourseService service;

  @Incoming(IncomingChannels.Course.COURSE_ADDED)
  @Transactional
  void courseAdded(CoursePayload payload) {
    log.info("Course added: %s".formatted(payload));
    service.add(payload);
  }

  @Incoming(IncomingChannels.Course.COURSE_DELETED)
  @Transactional
  void courseDeleted(CoursePayload payload) {
    log.info("Course deleted: %s".formatted(payload));
    service.delete(payload);
  }

  @Incoming(IncomingChannels.Course.COURSE_UPDATED)
  @Transactional
  void courseUpdated(CourseUpdated payload) {
    log.info("Course updated: %s".formatted(payload));
    service.update(payload);
  }

}
