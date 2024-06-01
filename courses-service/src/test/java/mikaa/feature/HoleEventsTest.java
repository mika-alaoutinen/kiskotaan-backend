package mikaa.feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import mikaa.domain.NewHole;
import mikaa.domain.UpdatedHole;
import mikaa.kiskotaan.course.CourseEvent;
import mikaa.kiskotaan.course.Hole;
import mikaa.producers.CourseProducer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import io.smallrye.reactive.messaging.kafka.Record;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySink;

@QuarkusTest
class HoleEventsTest {

  private static final long COURSE_ID = 321;
  private static final long HOLE_ID = 123;
  private static final int HOLE_NUMBER = 2;

  @Any
  @Inject
  private InMemoryConnector connector;

  @Inject
  private CourseProducer producer;

  @InjectMock
  private CourseFinder courseFinder;

  @InjectMock
  private HoleRepository repository;

  private InMemorySink<Record<Long, CourseEvent>> sink;
  private HoleService service;

  @BeforeEach
  void setup() {
    sink = connector.sink(CourseProducer.COURSE_STATE);
    sink.clear();
    service = new HoleService(courseFinder, producer, repository);
  }

  @Test
  void should_send_event_on_add() {
    when(courseFinder.findCourseOrThrow(anyLong())).thenReturn(courseMock());
    var newHole = new NewHole(1, 3, 100);
    service.add(COURSE_ID, newHole);

    assertEvent(new Hole(-1L, 1, 3, 100));
    verify(repository, atLeastOnce()).persist(any(HoleEntity.class));
  }

  @Test
  void should_send_event_on_update() {
    when(courseFinder.findCourseOrThrow(anyLong())).thenReturn(courseMock());
    service.update(COURSE_ID, HOLE_NUMBER, new UpdatedHole(5, 165));

    assertEvent(new Hole(HOLE_ID, 2, 5, 165));
  }

  @Test
  void should_send_event_on_delete() {
    when(repository.findByCourseIdAndNumber(anyLong(), anyInt())).thenReturn(Optional.of(holeMock()));
    service.delete(COURSE_ID, HOLE_NUMBER);

    assertEvent(new Hole(HOLE_ID, 2, 3, 123));
    verify(repository, atLeastOnce()).deleteById(anyLong());
  }

  private void assertEvent(Hole expected) {
    assertEquals(1, sink.received().size());
    var record = sink.received().get(0).getPayload();
    var payload = record.value().getPayload();

    assertEquals(payload.getId(), record.key());
    assertTrue(payload.getHoles().contains(expected));
  }

  private static CourseEntity courseMock() {
    var course = new CourseEntity(COURSE_ID, "DG Course", new ArrayList<HoleEntity>());
    course.addHole(new HoleEntity(HOLE_ID, 2, 3, 123, null));
    return course;
  }

  private static HoleEntity holeMock() {
    var course = courseMock();
    var hole = new HoleEntity(HOLE_ID, 2, 3, 123, course);
    course.addHole(hole);
    return hole;
  }

}
