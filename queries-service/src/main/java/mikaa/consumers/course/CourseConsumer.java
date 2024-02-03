package mikaa.consumers.course;

import org.eclipse.microprofile.reactive.messaging.Incoming;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mikaa.config.IncomingChannels;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.course.CourseEvent;
import mikaa.kiskotaan.course.CoursePayload;

@ApplicationScoped
@RequiredArgsConstructor
@Slf4j
class CourseConsumer {

  private final CourseWriter writer;

  @Incoming(IncomingChannels.COURSE_STATE)
  Uni<Void> consumeEvent(CourseEvent event) {
    var payload = event.getPayload();

    return switch (event.getAction()) {
      case ADD -> courseAdded(payload);
      case UPDATE -> courseUpdated(payload);
      case DELETE -> courseDeleted(payload);
      case UNKNOWN -> handleUnknown(event.getAction());
    };
  }

  private Uni<Void> courseAdded(CoursePayload payload) {
    log.info("received course added event {}", payload);
    return writer.add(payload).replaceWithVoid();
  }

  private Uni<Void> courseDeleted(CoursePayload payload) {
    log.info("received course deleted event: {}", payload);
    return writer.delete(payload).replaceWithVoid();
  }

  private Uni<Void> courseUpdated(CoursePayload payload) {
    log.info("received course updated event: {}", payload);
    return writer.update(payload).replaceWithVoid();
  }

  private Uni<Void> handleUnknown(Action action) {
    log.warn("Unknown course event type {}", action);
    return Uni.createFrom().voidItem();
  }

}
