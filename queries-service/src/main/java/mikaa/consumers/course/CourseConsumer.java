package mikaa.consumers.course;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.CoursePayload;
import mikaa.CourseUpdated;
import mikaa.config.IncomingChannels;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class CourseConsumer {

  @Incoming(IncomingChannels.Course.COURSE_ADDED)
  @Transactional
  void courseAdded(CoursePayload payload) {
    log.info("received course added event", payload);
  }

  @Incoming(IncomingChannels.Course.COURSE_DELETED)
  @Transactional
  void courseDeleted(CoursePayload payload) {
    log.info("received course deleted event", payload);
  }

  @Incoming(IncomingChannels.Course.COURSE_UPDATED)
  @Transactional
  void courseUpdated(CourseUpdated payload) {
    log.info("received course updated event", payload);
  }

}
