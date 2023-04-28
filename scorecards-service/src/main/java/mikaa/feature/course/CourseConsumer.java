package mikaa.feature.course;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.RequiredArgsConstructor;
import mikaa.events.course.CourseAdded;
import mikaa.events.course.CourseUpdated;

@ApplicationScoped
@RequiredArgsConstructor
class CourseConsumer {

  private static final Logger log = LoggerFactory.getLogger(CourseConsumer.class);
  private final CourseService service;

  @Incoming("course-added")
  @Transactional
  void courseAdded(CourseAdded payload) {
    log.info("Course added: %s".formatted(payload));
    service.add(payload);
  }

  @Incoming("course-deleted")
  @Transactional
  void courseDeleted(long courseId) {
    log.info("Course deleted: %s".formatted(courseId));
    service.delete(courseId);
  }

  @Incoming("course-updated")
  @Transactional
  void courseUpdated(CourseUpdated payload) {
    log.info("Course updated: %s".formatted(payload));
    service.update(payload);
  }

}
