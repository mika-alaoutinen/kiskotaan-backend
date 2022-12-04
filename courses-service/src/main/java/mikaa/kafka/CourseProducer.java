package mikaa.kafka;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import mikaa.dto.CourseDTO;
import mikaa.dto.HoleDTO;

@ApplicationScoped
public class CourseProducer {

  @Inject
  @Channel("courses-out")
  Emitter<CourseEvent> courseEmitter;

  @Inject
  @Channel("courses-out")
  Emitter<HoleEvent> holeEmitter;

  public void send(CourseEventType type, CourseDTO course) {
    courseEmitter.send(new CourseEvent(type, course));
  }

  public void send(HoleEventType type, HoleDTO hole) {
    holeEmitter.send(new HoleEvent(type, hole));
  }

}
