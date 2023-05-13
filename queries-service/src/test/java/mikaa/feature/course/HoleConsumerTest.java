package mikaa.feature.course;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySource;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import mikaa.HolePayload;
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

  private void sendAddEvent() {
    InMemorySource<HolePayload> source = connector.source(IncomingChannels.Hole.HOLE_ADDED);
    source.send(PAYLOAD);
  }

  private void sendDeleteEvent() {
    InMemorySource<HolePayload> source = connector.source(IncomingChannels.Hole.HOLE_DELETED);
    source.send(PAYLOAD);
  }

  private void sendUpdateEvent() {
    InMemorySource<HolePayload> source = connector.source(IncomingChannels.Hole.HOLE_UPDATED);
    source.send(PAYLOAD);
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
