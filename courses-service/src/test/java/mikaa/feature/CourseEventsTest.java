package mikaa.feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import javax.enterprise.inject.Any;
import javax.inject.Inject;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.reactive.messaging.providers.connectors.InMemoryConnector;
import io.smallrye.reactive.messaging.providers.connectors.InMemorySink;
import mikaa.dto.CourseDTO;
import mikaa.dto.NewCourseDTO;
import mikaa.dto.NewHoleDTO;
import mikaa.kafka.courses.CourseEvent;
import mikaa.kafka.courses.CourseProducer;

@QuarkusTest
class CourseEventsTest {

  @Any
  @Inject
  private InMemoryConnector connector;

  @Inject
  private CourseProducer producer;

  @InjectMock
  private CourseRepository repository;

  private InMemorySink<CourseEvent> sink;
  private CourseService service;

  @BeforeEach
  void setup() {
    sink = connector.sink("courses-out");
    sink.clear();
    service = new CourseService(producer, repository);
  }

  @AfterEach
  void teardown() {
    sink.clear();
  }

  @Test
  void should_send_event_on_add() {
    var course = new NewCourseDTO("New Course", List.of(new NewHoleDTO(1, 3, 90)));
    service.add(course);

    var payload = assertEvent("COURSE_ADDED", "New Course");
    assertEquals(1, payload.holes().size());
    verify(repository, atLeastOnce()).persist(any(CourseEntity.class));
  }

  @Test
  void should_send_event_on_course_name_update() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(courseMock()));
    service.updateCourseName(1, "Updated Name");

    var payload = assertEvent("COURSE_UPDATED", "Updated Name");
    assertTrue(payload.holes().isEmpty());
    verify(repository, atLeastOnce()).persist(any(CourseEntity.class));
  }

  @Test
  void should_send_event_on_delete() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(courseMock()));    
    service.delete(1);

    var payload = assertEvent("COURSE_DELETED", "DG Course");
    assertTrue(payload.holes().isEmpty());
    verify(repository, atLeastOnce()).deleteById(1L);
  }

  private CourseDTO assertEvent(String eventName, String courseName) {
    assertEquals(1, sink.received().size());
    var event = sink.received().get(0).getPayload();

    assertEquals(eventName, event.type().toString());
    assertEquals(courseName, event.payload().name());

    return event.payload();
  }

  private static CourseEntity courseMock() {
    return new CourseEntity(1L, "DG Course", List.of());
  }

}
