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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySink;
import mikaa.kafka.holes.HoleEvent;
import mikaa.kafka.holes.HolePayload;
import mikaa.kafka.holes.HoleProducer;

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

  private InMemorySink<HoleEvent> sink;
  private HoleService service;

  @BeforeEach
  void setup() {
    sink = connector.sink("holes-out");
    sink.clear();
    service = new HoleService(courseService, producer, repository);
  }

  @AfterEach
  void teardown() {
    sink.clear();
  }

  @Test
  void should_send_event_on_add() {
    when(courseService.findOne(anyLong())).thenReturn(courseMock());
    var newHole = new HoleEntity(null, 1, 3, 100, courseMock());
    service.add(COURSE_ID, newHole);

    // holeId is null because mocked repository does not create a new ID on persist
    var expected = new HolePayload(null, COURSE_ID, 1, 3, 100);
    assertEvent("HOLE_ADDED", expected);
    verify(repository, atLeastOnce()).persist(any(HoleEntity.class));
  }

  @Test
  void should_send_event_on_update() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(holeMock()));
    service.update(HOLE_ID, new HoleEntity(null, 4, 5, 165, courseMock()));

    var expected = new HolePayload(HOLE_ID, COURSE_ID, 4, 5, 165);
    assertEvent("HOLE_UPDATED", expected);
  }

  @Test
  void should_send_event_on_delete() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(holeMock()));
    service.delete(HOLE_ID);

    var expected = new HolePayload(HOLE_ID, COURSE_ID, 2, 3, 123);
    assertEvent("HOLE_DELETED", expected);
    verify(repository, atLeastOnce()).deleteById(anyLong());
  }

  private void assertEvent(String eventName, HolePayload expected) {
    assertEquals(1, sink.received().size());
    var event = sink.received().get(0).getPayload();

    assertEquals(eventName, event.type().toString());
    assertEquals(expected.id(), event.payload().id());
    assertEquals(expected.courseId(), event.payload().courseId());
    assertEquals(expected.number(), event.payload().number());
    assertEquals(expected.distance(), event.payload().distance());
    assertEquals(expected.par(), event.payload().par());
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
