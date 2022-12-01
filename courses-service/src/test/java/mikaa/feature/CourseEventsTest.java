package mikaa.feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import mikaa.dto.NewCourseDTO;
import mikaa.events.CourseEvents.CourseEvent;
import mikaa.kafka.CourseProducer;

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
    service = new CourseService(producer, repository);
  }

  @AfterEach
  void reset() {
    sink.clear();
  }

  @Test
  void should_send_event_on_add() {
    service.add(new NewCourseDTO("New Course", List.of()));
    assertEquals(1, sink.received().size());
    assertEvent("COURSE_ADDED", "New Course");
    verify(repository, atLeastOnce()).persist(any(CourseEntity.class));
  }

  @Test
  void should_send_event_on_course_name_update() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(courseMock()));
    service.updateCourseName(1, "Updated Name");
    assertEvent("COURSE_UPDATED", "Updated Name");
    verify(repository, atLeastOnce()).persist(any(CourseEntity.class));
  }

  @Test
  void should_send_event_on_delete() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(courseMock()));    
    service.delete(1);
    assertEvent("COURSE_DELETED", "DG Course");
    verify(repository, atLeastOnce()).deleteById(1L);
  }

  private void assertEvent(String eventName, String courseName) {
    assertEquals(1, sink.received().size());
    var event = sink.received().get(0).getPayload();
    assertEquals(eventName, event.type().toString());
    assertEquals(courseName, event.course().name());
  }

  private static CourseEntity courseMock() {
    return new CourseEntity(1L, "DG Course", List.of());
  }

}
