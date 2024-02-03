package mikaa.feature.course;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySource;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.course.HoleEvent;
import mikaa.kiskotaan.course.HolePayload;
import mikaa.config.IncomingChannels;

@QuarkusTest
class HoleConsumerTest {

  static final HolePayload PAYLOAD = new HolePayload(9l, 1l, 2, 3, 75);

  @InjectMock
  CourseRepository repository;

  @Any
  @Inject
  InMemoryConnector connector;

  @Test
  void handles_add_hole_event() {
    when(repository.findByExternalId(1)).thenReturn(courseUni());
    sendAddEvent();
    verifyUpdate();
  }

  @Test
  void does_nothing_on_add_if_course_not_found() {
    sendAddEvent();
    verifyNoUpdate();
  }

  @Test
  void handles_delete_hole_event() {
    when(repository.findByExternalId(1)).thenReturn(courseUni());
    sendDeleteEvent();
    verifyUpdate();
  }

  @Test
  void does_nothing_on_delete_if_course_not_found() {
    sendDeleteEvent();
    verifyNoUpdate();
  }

  @Test
  void handles_update_hole_event() {
    when(repository.findByExternalId(1)).thenReturn(courseUni());
    sendUpdateEvent();
    verifyUpdate();
  }

  @Test
  void does_nothing_on_update_if_course_not_found() {
    sendUpdateEvent();
    verifyNoUpdate();
  }

  @Test
  void ignores_unknown_event_types() {
    sendEvent(new HoleEvent(Action.UNKNOWN, null));
    verifyNoInteractions(repository);
  }

  private void sendEvent(HoleEvent event) {
    InMemorySource<HoleEvent> source = connector.source(IncomingChannels.HOLE_STATE);
    source.send(event);
  }

  private void sendAddEvent() {
    sendEvent(new HoleEvent(Action.ADD, PAYLOAD));
  }

  private void sendDeleteEvent() {
    sendEvent(new HoleEvent(Action.DELETE, PAYLOAD));
  }

  private void sendUpdateEvent() {
    sendEvent(new HoleEvent(Action.UPDATE, PAYLOAD));
  }

  private void verifyUpdate() {
    verify(repository, atLeastOnce()).update(any(CourseEntity.class));
  }

  private void verifyNoUpdate() {
    verify(repository, never()).update(any(CourseEntity.class));
  }

  private static Uni<CourseEntity> courseUni() {
    var holes = new ArrayList<HoleEntity>();
    holes.add(new HoleEntity(1l, 1, 4, 90));
    var laajis = new CourseEntity(1l, "Laajis", holes);
    return Uni.createFrom().item(laajis);
  }

}
