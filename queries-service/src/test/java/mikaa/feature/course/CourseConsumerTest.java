package mikaa.feature.course;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySource;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import mikaa.kiskotaan.domain.Action;
import mikaa.kiskotaan.course.CourseEvent;
import mikaa.kiskotaan.course.CoursePayload;
import mikaa.kiskotaan.course.HoleEvent;
import mikaa.config.IncomingChannels;

@QuarkusTest
class CourseConsumerTest {

  static final Uni<CourseEntity> LAAJIS_UNI = Uni.createFrom().item(new CourseEntity(1l, "Laajis", List.of()));

  @InjectMock
  CourseRepository repository;

  @Any
  @Inject
  InMemoryConnector connector;

  @Test
  void handles_add_course_event() {
    var payload = new CoursePayload(1l, "New Course", List.of());
    sendEvent(new CourseEvent(Action.ADD, payload));
    verify(repository, atLeastOnce()).persist(any(CourseEntity.class));
  }

  @Test
  void handles_delete_course_event() {
    when(repository.findByExternalId(anyLong())).thenReturn(LAAJIS_UNI);
    sendDeleteEvent();
    verify(repository, atLeastOnce()).delete(any(CourseEntity.class));
  }

  @Test
  void does_nothing_on_delete_if_course_not_found() {
    sendDeleteEvent();
    verify(repository, never()).delete(any(CourseEntity.class));
  }

  @Test
  void handles_update_course_event() {
    when(repository.findByExternalId(anyLong())).thenReturn(LAAJIS_UNI);
    sendUpdateEvent();
    verify(repository, atLeastOnce()).update(any(CourseEntity.class));
  }

  @Test
  void does_nothing_on_update_if_course_not_found() {
    sendUpdateEvent();
    verify(repository, never()).update(any(CourseEntity.class));
  }

  @Test
  void ignores_unknown_event_types() {
    sendEvent(new CourseEvent(Action.UNKNOWN, null));
    verifyNoInteractions(repository);
  }

  private void sendEvent(CourseEvent event) {
    InMemorySource<CourseEvent> source = connector.source(IncomingChannels.COURSE_STATE);
    source.send(event);
  }

  private void sendDeleteEvent() {
    var payload = new CoursePayload(1l, "Laajis", List.of());
    sendEvent(new CourseEvent(Action.DELETE, payload));
  }

  private void sendUpdateEvent() {
    var payload = new CoursePayload(1l, "Laajis v2", Collections.emptyList());
    sendEvent(new CourseEvent(Action.UPDATE, payload));
  }

}
