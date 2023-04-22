package mikaa.feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import mikaa.events.holes.HolePayload;
import mikaa.events.holes.HoleProducer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySink;

@QuarkusTest
class HoleEventsTest {

  private static final long COURSE_ID = 321;
  private static final long HOLE_ID = 123;

  @Any
  @Inject
  private InMemoryConnector connector;

  @Inject
  private HoleProducer producer;

  @InjectMock
  private CourseService courseService;

  @InjectMock
  private HoleRepository repository;

  private HoleService service;

  @BeforeEach
  void setup() {
    service = new HoleService(courseService, producer, repository);
  }

  @Test
  void should_send_event_on_add() {
    InMemorySink<HolePayload> sink = connector.sink("hole-added");

    when(courseService.findOne(anyLong())).thenReturn(courseMock());
    var newHole = new HoleEntity(null, 1, 3, 100, courseMock());
    service.add(COURSE_ID, newHole);

    // holeId is null because mocked repository does not create a new ID on persist
    assertEvent(sink, new HolePayload(null, COURSE_ID, 1, 3, 100));
    verify(repository, atLeastOnce()).persist(any(HoleEntity.class));
  }

  @Test
  void should_send_event_on_update() {
    InMemorySink<HolePayload> sink = connector.sink("hole-updated");

    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(holeMock()));
    service.update(HOLE_ID, new HoleEntity(null, 4, 5, 165, courseMock()));

    assertEvent(sink, new HolePayload(HOLE_ID, COURSE_ID, 4, 5, 165));
  }

  @Test
  void should_send_event_on_delete() {
    InMemorySink<Long> sink = connector.sink("hole-deleted");

    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(holeMock()));
    service.delete(HOLE_ID);

    assertEquals(1, sink.received().size());
    var payload = sink.received().get(0).getPayload();
    assertEquals(HOLE_ID, payload);
    verify(repository, atLeastOnce()).deleteById(anyLong());
  }

  private void assertEvent(InMemorySink<HolePayload> sink, HolePayload expected) {
    assertEquals(1, sink.received().size());
    var payload = sink.received().get(0).getPayload();

    assertEquals(expected.id(), payload.id());
    assertEquals(expected.courseId(), payload.courseId());
    assertEquals(expected.number(), payload.number());
    assertEquals(expected.distance(), payload.distance());
    assertEquals(expected.par(), payload.par());
  }

  private static CourseEntity courseMock() {
    return new CourseEntity(COURSE_ID, "DG Course", new ArrayList<>());
  }

  private static HoleEntity holeMock() {
    var course = courseMock();
    var hole = new HoleEntity(HOLE_ID, 2, 3, 123, course);
    course.addHole(hole);
    return hole;
  }

}
