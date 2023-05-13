package mikaa.consumers.course;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.CoursePayload;
import mikaa.CourseUpdated;
import mikaa.config.IncomingChannels;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class CourseConsumer {

  private final CourseWriter writer;

  @Incoming(IncomingChannels.Course.COURSE_ADDED)
  Uni<Void> courseAdded(CoursePayload payload) {
    log.info("received course added event", payload);
    return writer.add(payload).replaceWithVoid();
  }

  @Incoming(IncomingChannels.Course.COURSE_DELETED)
  Uni<Void> courseDeleted(CoursePayload payload) {
    log.info("received course deleted event", payload);
    return writer.delete(payload).replaceWithVoid();
  }

  @Incoming(IncomingChannels.Course.COURSE_UPDATED)
  Uni<Void> courseUpdated(CourseUpdated payload) {
    log.info("received course updated event", payload);
    return writer.update(payload).replaceWithVoid();
  }

}
