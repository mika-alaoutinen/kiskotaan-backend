package mikaa.kafka;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import mikaa.events.CourseEvents.CourseEvent;

@ApplicationScoped
public class CourseProducer {

  @Inject
  @Channel("courses-out")
  Emitter<CourseEvent> emitter;

  public void send(CourseEvent event) {
    emitter.send(event);
  }

}
