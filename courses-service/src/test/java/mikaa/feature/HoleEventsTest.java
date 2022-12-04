package mikaa.feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import mikaa.dto.NewHoleDTO;
import mikaa.kafka.CourseProducer;
import mikaa.kafka.HoleEvent;

@QuarkusTest
class HoleEventsTest {

  @Any
  @Inject
  private InMemoryConnector connector;

  @Inject
  private CourseProducer producer;

  @InjectMock
  private CourseRepository courseRepository;

  @InjectMock
  private HoleRepository repository;

  private InMemorySink<HoleEvent> sink;
  private HoleService service;

  @BeforeEach
  void setup() {
    sink = connector.sink("courses-out");
    sink.clear();
    service = new HoleService(producer, courseRepository, repository);
  }

  @AfterEach
  void teardown() {
    sink.clear();
  }

  @Test
  void should_send_event_on_add() {
    when(courseRepository.findByIdOptional(anyLong())).thenReturn(Optional.of(courseMock()));
    service.add(1L, new NewHoleDTO(1, 3, 100));
    assertEvent("HOLE_ADDED", 1L);
    verify(repository, atLeastOnce()).persist(any(HoleEntity.class));
  }

  private void assertEvent(String eventName, Long courseId) {
    assertEquals(1, sink.received().size());
    var event = sink.received().get(0).getPayload();
    assertEquals(eventName, event.type().toString());
    assertEquals(courseId, event.hole().courseId());
  }

  private static CourseEntity courseMock() {
    return new CourseEntity(1L, "DG Course", new ArrayList<>());
  }

  private static HoleEntity holeMock() {
    var course = courseMock();
    var hole = new HoleEntity(1L, 1, 3, 100, course);
    course.addHole(hole);
    return hole;
  }

}
