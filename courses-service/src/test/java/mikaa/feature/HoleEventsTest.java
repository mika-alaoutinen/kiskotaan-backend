package mikaa.feature;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import mikaa.kiskotaan.course.HoleEvent;
import mikaa.kiskotaan.course.HolePayload;
import mikaa.producers.OutgoingChannels;
import mikaa.producers.holes.HoleProducer;

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
  private HoleProducer producer;

  @InjectMock
  private CourseFinder courseFinder;

  @InjectMock
  private HoleRepository repository;

  private InMemorySink<Record<Long, HoleEvent>> sink;
  private HoleService service;

  @BeforeEach
  void setup() {
    sink = connector.sink(OutgoingChannels.HOLE_STATE);
    sink.clear();
    service = new HoleService(courseFinder, producer, repository);
  }

  @Test
  void should_send_event_on_add() {
    when(courseFinder.findCourseOrThrow(anyLong())).thenReturn(courseMock());
    var newHole = new NewHole(1, 3, 100);
    service.add(COURSE_ID, newHole);

    assertEvent(new HolePayload(-1L, COURSE_ID, 1, 3, 100));
    verify(repository, atLeastOnce()).persist(any(HoleEntity.class));
  }

  @Test
  void should_send_event_on_update() {
    when(courseFinder.findCourseOrThrow(anyLong())).thenReturn(courseMock());
    service.update(COURSE_ID, HOLE_NUMBER, new UpdatedHole(5, 165));

    assertEvent(new HolePayload(HOLE_ID, COURSE_ID, 2, 5, 165));
  }

  @Test
  void should_send_event_on_delete() {
    when(repository.findByCourseIdAndNumber(anyLong(), anyInt())).thenReturn(Optional.of(holeMock()));
    service.delete(COURSE_ID, HOLE_NUMBER);

    assertEvent(new HolePayload(HOLE_ID, COURSE_ID, 2, 3, 123));
    verify(repository, atLeastOnce()).deleteById(anyLong());
  }

  private void assertEvent(HolePayload expected) {
    assertEquals(1, sink.received().size());
    var record = sink.received().get(0).getPayload();
    var payload = record.value().getPayload();

    assertEquals(payload.getId(), record.key());

    assertEquals(expected.getId(), payload.getId());
    assertEquals(expected.getCourseId(), payload.getCourseId());
    assertEquals(expected.getNumber(), payload.getNumber());
    assertEquals(expected.getDistance(), payload.getDistance());
    assertEquals(expected.getPar(), payload.getPar());
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
