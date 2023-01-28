package mikaa.feature.course;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
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
import mikaa.events.course.CourseEvent;
import mikaa.events.course.CourseEventType;
import mikaa.events.course.CoursePayload;
import mikaa.events.course.Hole;

@QuarkusTest
class CourseEventsTest {

  private static final CoursePayload LAAJIS = new CoursePayload(111, "Laajis", List.of());

  @Any
  @Inject
  private InMemoryConnector connector;

  @InjectMock
  private CourseRepository repository;

  private InMemorySource<CourseEvent> source;

  @BeforeEach
  void setup() {
    source = connector.source("courses-in");
  }

  @Test
  void should_save_new_course() {
    var hole = new Hole(222, 1, 3, 85);
    var newCourse = new CoursePayload(111L, "Laajis", List.of(hole));

    source.send(new CourseEvent(CourseEventType.COURSE_ADDED, newCourse));
    var persistedEntity = new CourseEntity(null, 1, "Laajis", Set.of());
    verify(repository, atLeastOnce()).persist(persistedEntity);
  }

  @Test
  void should_update_course() {
    var course = new CourseEntity(222L, 18, "Laajis", Set.of());
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(course));

    source.send(new CourseEvent(CourseEventType.COURSE_UPDATED, LAAJIS));
    verify(repository, atLeastOnce()).persist(any(CourseEntity.class));
  }

  @Test
  void should_do_nothing_on_update_if_course_not_found() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.empty());

    source.send(new CourseEvent(CourseEventType.COURSE_UPDATED, LAAJIS));
    verify(repository, never()).persist(any(CourseEntity.class));
  }

  @Test
  void should_delete_course() {
    source.send(new CourseEvent(CourseEventType.COURSE_UPDATED, LAAJIS));
    verify(repository, atLeastOnce()).deleteById(111L);
  }

}
