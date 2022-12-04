package mikaa.kafka;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import mikaa.dto.CourseDTO;

@ApplicationScoped
public class KafkaProducer {

  @Inject
  @Channel("courses-out")
  Emitter<CourseEvent> emitter;

  public void send(EventType type, CourseDTO course) {
    var acked = emitter.send(new CourseEvent(type, course));
    acked.toCompletableFuture().join();
  }

}
