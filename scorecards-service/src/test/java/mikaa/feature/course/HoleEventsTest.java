package mikaa.feature.course;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.reactive.messaging.providers.connectors.InMemoryConnector;
import io.smallrye.reactive.messaging.providers.connectors.InMemorySource;
import mikaa.events.hole.HoleEvent;
import mikaa.events.hole.HoleEventType;
import mikaa.events.hole.HolePayload;

@QuarkusTest
class HoleEventsTest {

  static final CourseEntity LAAJIS = new CourseEntity(111L, 18, "Laajis", Set.of());
  static final HolePayload HOLE = new HolePayload(111L, 123L, 1, 3, 85);

  @Any
  @Inject
  InMemoryConnector connector;

  @InjectMock
  CourseRepository repository;

  InMemorySource<HoleEvent> source;

  @BeforeEach
  void setup() {
    source = connector.source("holes-in");
  }

  @Test
  void should_increment_hole_count_by_one() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(LAAJIS));
    source.send(new HoleEvent(HoleEventType.HOLE_ADDED, HOLE));
    verify(repository, atLeastOnce()).persist(any(CourseEntity.class));
  }

  @Test
  void should_do_nothing_on_add_if_course_not_found() {
    source.send(new HoleEvent(HoleEventType.HOLE_ADDED, HOLE));
    verify(repository, never()).persist(any(CourseEntity.class));
  }

  @Test
  void should_decrement_hole_count_by_one() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(LAAJIS));
    source.send(new HoleEvent(HoleEventType.HOLE_DELETED, HOLE));
    verify(repository, atLeastOnce()).deleteById(111L);
  }

  @Test
  void should_do_nothing_on_delete_if_course_not_found() {
    source.send(new HoleEvent(HoleEventType.HOLE_DELETED, HOLE));
    verify(repository, never()).persist(any(CourseEntity.class));
  }

}
