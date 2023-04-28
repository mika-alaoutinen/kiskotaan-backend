package mikaa.feature.course;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.events.course.CourseAdded;
import mikaa.events.course.CourseUpdated;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class CourseConsumer {

  private final CourseService service;

  @Incoming("course-added")
  @Transactional
  void courseAdded(CourseAdded payload) {
    log.info("Course added: %s".formatted(payload));
    service.add(payload);
  }

  @Incoming("course-deleted")
  @Transactional
  void courseDeleted(long id) {
    log.info("Course deleted: %s".formatted(id));
    service.delete(id);
  }

  @Incoming("course-updated")
  @Transactional
  void courseUpdated(CourseUpdated payload) {
    log.info("Course updated: %s".formatted(payload));
    service.update(payload);
  }

}
