package mikaa.feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import mikaa.events.courses.CourseAdded;
import mikaa.events.courses.CourseProducer;
import mikaa.events.courses.CourseUpdated;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySink;

@QuarkusTest
class CourseEventsTest {

  @Any
  @Inject
  private InMemoryConnector connector;

  @Inject
  private CourseProducer producer;

  @Inject
  private CourseValidator validator;

  @InjectMock
  private CourseRepository repository;

  private CourseService service;

  @BeforeEach
  void setup() {
    service = new CourseService(producer, repository, validator);
  }

  @Test
  void should_send_event_on_add() {
    InMemorySink<CourseAdded> sink = connector.sink("course-added");

    var course = new CourseEntity(1L, "New Course", List.of(new HoleEntity(1L, 1, 3, 90, null)));
    service.add(course);

    assertEquals(1, sink.received().size());
    var payload = sink.received().get(0).getPayload();

    assertEquals("New Course", payload.name());
    assertEquals(1, payload.holes().size());

    verify(repository, atLeastOnce()).persist(any(CourseEntity.class));
  }

  @Test
  void should_send_event_on_course_name_update() {
    InMemorySink<CourseUpdated> sink = connector.sink("course-updated");

    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(courseMock()));
    service.updateCourseName(1, "Updated Name");

    assertEquals(1, sink.received().size());
    var payload = sink.received().get(0).getPayload();

    assertEquals("Updated Name", payload.name());
  }

  @Test
  void should_send_event_on_delete() {
    InMemorySink<Long> sink = connector.sink("course-deleted");

    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(courseMock()));
    service.delete(1);

    assertEquals(1, sink.received().size());
    var payload = sink.received().get(0).getPayload();

    assertEquals(1, payload);
  }

  private static CourseEntity courseMock() {
    return new CourseEntity(1L, "DG Course", List.of());
  }

}
