package mikaa.producers.courses;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import mikaa.kiskotaan.domain.Action;
import mikaa.domain.Course;
import mikaa.kiskotaan.course.CourseEvent;
import mikaa.kiskotaan.course.CoursePayload;
import mikaa.kiskotaan.course.Hole;
import mikaa.producers.OutgoingChannels;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import io.smallrye.reactive.messaging.kafka.Record;

@ApplicationScoped
class KafkaProducer implements CourseProducer {

  @Inject
  @Channel(OutgoingChannels.COURSE_STATE)
  Emitter<Record<Long, CourseEvent>> emitter;

  @Override
  public void courseAdded(Course course) {
    send(Action.ADD, course);
  }

  @Override
  public void courseUpdated(Course course) {
    send(Action.UPDATE, course);
  }

  @Override
  public void courseDeleted(Course course) {
    send(Action.DELETE, course);
  }

  private void send(Action action, Course course) {
    var holes = course.holes()
        .stream()
        .map(hole -> new Hole(hole.id(), hole.number(), hole.par(), hole.distance()))
        .toList();

    var payload = new CoursePayload(course.id(), course.name(), holes);
    var record = Record.of(payload.getId(), new CourseEvent(action, payload));

    emitter.send(record).toCompletableFuture().join();
  }

}
