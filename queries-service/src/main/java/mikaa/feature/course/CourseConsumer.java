package mikaa.feature.course;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class CourseConsumer {

  @Incoming("course-added")
  @Transactional
  void courseAdded(String event) {
    log.info("received course added event", event);
  }

  @Incoming("course-deleted")
  @Transactional
  void courseDeleted(String event) {
    log.info("received course deleted event", event);
  }

  @Incoming("course-updated")
  @Transactional
  void courseUpdated(String event) {
    log.info("received course updated event", event);
  }

}
