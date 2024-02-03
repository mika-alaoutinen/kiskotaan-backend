package mikaa.feature.course;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import mikaa.config.IncomingChannels;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.course.HoleEvent;
import mikaa.kiskotaan.course.HolePayload;

@QuarkusTest
class HoleConsumerTest {

  @InjectMock
  CourseRepository repository;

  @Any
  @Inject
  InMemoryConnector connector;

  @Test
  void handles_add_hole_event() throws InterruptedException {
    when(repository.findByExternalId(1)).thenReturn(Optional.of(mockCourse()));
    var payload = new HolePayload(333L, 1L, 2, 4, 100);
    sendEvent(new HoleEvent(Action.ADD, payload));
    verify(repository, atLeastOnce()).persist(any(CourseEntity.class));
  }

  @Test
  void ignores_upate_hole_events() throws InterruptedException {
    var payload = new HolePayload(333L, 1L, 2, 4, 100);
    sendEvent(new HoleEvent(Action.UPDATE, payload));
    verifyNoInteractions(repository);
  }

  @Test
  void handles_delete_hole_event() throws InterruptedException {
    when(repository.findByExternalId(1)).thenReturn(Optional.of(mockCourse()));
    sendDeleteEvent();
    verify(repository, atLeastOnce()).persist(new CourseEntity(1L, "Laajis"));
  }

  @Test
  void does_nothing_on_delete_if_course_not_found() throws InterruptedException {
    sendDeleteEvent();
    verify(repository, never()).delete(any(CourseEntity.class));
  }

  @Test
  void ignores_unknown_event_types() throws InterruptedException {
    sendEvent(new HoleEvent(Action.UNKNOWN, null));
    verifyNoInteractions(repository);
  }

  private void sendEvent(HoleEvent event) throws InterruptedException {
    var source = connector.source(IncomingChannels.HOLE_STATE);
    source.send(event);
    // I guess in-memory test channels don't work so great with blocking code
    Thread.sleep(500);
  }

  private void sendDeleteEvent() throws InterruptedException {
    var payload = new HolePayload(123L, 1L, 1, 3, 80);
    sendEvent(new HoleEvent(Action.DELETE, payload));
  }

  private static CourseEntity mockCourse() {
    var holes = new ArrayList<HoleEntity>();
    holes.add(new HoleEntity(1, 3));
    return new CourseEntity(1L, holes, "Laajis");
  }

}
