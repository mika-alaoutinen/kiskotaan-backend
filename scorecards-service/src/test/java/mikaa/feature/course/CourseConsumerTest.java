package mikaa.feature.course;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.course.CourseEvent;
import mikaa.kiskotaan.course.CoursePayload;
import mikaa.kiskotaan.course.Hole;

@QuarkusTest
class CourseConsumerTest {

  static final CourseEntity LAAJIS = new CourseEntity(1, "Laajis");

  @InjectMock
  CourseRepository repository;

  @Any
  @Inject
  InMemoryConnector connector;

  @Test
  void handles_add_course_event() throws InterruptedException {
    var payload = new CoursePayload(222l, "New Course", List.of(new Hole(333L, 1, 4, 100)));
    sendEvent(new CourseEvent(Action.ADD, payload));
    var expectedCourse = new CourseEntity(222, List.of(new HoleEntity(1, 4)), "New Course");
    verify(repository, atLeastOnce()).persist(expectedCourse);
  }

  @Test
  void handles_upate_course_name() throws InterruptedException {
    when(repository.findByExternalId(1)).thenReturn(Optional.of(LAAJIS));
    sendUpdateEvent();
    var expectedCourse = new CourseEntity(1, "Laajavuori");
    verify(repository, atLeastOnce()).persist(expectedCourse);
  }

  @Test
  void handles_add_hole_event() throws InterruptedException {
    when(repository.findByExternalId(1)).thenReturn(Optional.of(LAAJIS));
    sendUpdateEvent(List.of(new Hole(2L, 18, 4, 120)));
    var expectedCourse = new CourseEntity(1, List.of(new HoleEntity(18, 4)), "Laajavuori");
    verify(repository, atLeastOnce()).persist(expectedCourse);
  }

  @Test
  void handles_update_hole_event() throws InterruptedException {
    var existingCourse = new CourseEntity(1, List.of(new HoleEntity(1, 3)), "Laajis");
    when(repository.findByExternalId(1)).thenReturn(Optional.of(existingCourse));

    sendUpdateEvent(List.of(new Hole(2L, 1, 5, 210)));
    var expectedCourse = new CourseEntity(1, List.of(new HoleEntity(1, 5)), "Laajavuori");
    verify(repository, atLeastOnce()).persist(expectedCourse);
  }

  @Test
  void handles_delete_hole_event() throws InterruptedException {
    var existingHoles = List.of(new HoleEntity(1, 3), new HoleEntity(2, 4));
    var existingCourse = new CourseEntity(1, existingHoles, "Laajis");
    when(repository.findByExternalId(1)).thenReturn(Optional.of(existingCourse));

    sendUpdateEvent(List.of(new Hole(2L, 1, 3, 120)));
    var expectedCourse = new CourseEntity(1, List.of(new HoleEntity(1, 3)), "Laajavuori");
    verify(repository, atLeastOnce()).persist(expectedCourse);
  }

  @Test
  void does_nothing_on_update_if_course_not_found() throws InterruptedException {
    sendUpdateEvent();
    verify(repository, never()).persist(any(CourseEntity.class));
  }

  @Test
  void handles_delete_course_event() throws InterruptedException {
    when(repository.findByExternalId(1)).thenReturn(Optional.of(LAAJIS));
    sendDeleteEvent();
    verify(repository, atLeastOnce()).deleteByExternalId(1);
  }

  @Test
  void does_nothing_on_delete_if_course_not_found() throws InterruptedException {
    sendDeleteEvent();
    verify(repository, never()).delete(any(CourseEntity.class));
  }

  @Test
  void ignores_unknown_event_types() throws InterruptedException {
    sendEvent(new CourseEvent(Action.UNKNOWN, null));
    verifyNoInteractions(repository);
  }

  private void sendEvent(CourseEvent event) throws InterruptedException {
    var source = connector.source(CourseConsumer.COURSE_STATE);
    source.send(event);
    // I guess in-memory test channels don't work so great with blocking code
    Thread.sleep(500);
  }

  private void sendUpdateEvent() throws InterruptedException {
    var payload = new CoursePayload(1L, "Laajavuori", List.of());
    sendEvent(new CourseEvent(Action.UPDATE, payload));
  }

  private void sendUpdateEvent(List<Hole> holes) throws InterruptedException {
    var payload = new CoursePayload(1L, "Laajavuori", holes);
    sendEvent(new CourseEvent(Action.UPDATE, payload));
  }

  private void sendDeleteEvent() throws InterruptedException {
    var payload = new CoursePayload(1L, "Laajis", List.of());
    sendEvent(new CourseEvent(Action.DELETE, payload));
  }

}
