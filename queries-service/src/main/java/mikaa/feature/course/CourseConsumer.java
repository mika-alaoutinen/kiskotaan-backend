package mikaa.feature.course;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.events.IncomingChannels;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class CourseConsumer {

  @Incoming(IncomingChannels.Course.COURSE_ADDED)
  @Transactional
  void courseAdded(String event) {
    log.info("received course added event", event);
  }

  @Incoming(IncomingChannels.Course.COURSE_DELETED)
  @Transactional
  void courseDeleted(String event) {
    log.info("received course deleted event", event);
  }

  @Incoming(IncomingChannels.Course.COURSE_UPDATED)
  @Transactional
  void courseUpdated(String event) {
    log.info("received course updated event", event);
  }

}
