package mikaa.feature.course;

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
import mikaa.feature.hole.HoleEntity;
import mikaa.kiskotaan.domain.CoursePayload;
import mikaa.kiskotaan.domain.CourseUpdated;
import mikaa.producers.OutgoingChannels;
import mikaa.producers.courses.CourseProducer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.smallrye.reactive.messaging.kafka.Record;
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
    InMemorySink<Record<Long, CoursePayload>> sink = connector.sink(OutgoingChannels.Course.COURSE_ADDED);

    var course = new CourseEntity(1L, "New Course", List.of(new HoleEntity(1L, 1, 3, 90, null)));
    service.add(course);

    assertEvent(sink, "New Course", 1);
    verify(repository, atLeastOnce()).persist(any(CourseEntity.class));
  }

  @Test
  void should_send_event_on_course_name_update() {
    InMemorySink<Record<Long, CourseUpdated>> sink = connector.sink(OutgoingChannels.Course.COURSE_UPDATED);

    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(courseMock()));
    service.updateCourseName(1, "Updated Name");

    assertEquals(1, sink.received().size());
    var record = sink.received().get(0).getPayload();
    assertEquals(1L, record.key());
    assertEquals("Updated Name", record.value().getName());
  }

  @Test
  void should_send_event_on_delete() {
    InMemorySink<Record<Long, CoursePayload>> sink = connector.sink(OutgoingChannels.Course.COURSE_DELETED);

    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(courseMock()));
    service.delete(1);

    assertEvent(sink, "DG Course", 0);
    verify(repository, atLeastOnce()).deleteById(anyLong());
  }

  private static void assertEvent(InMemorySink<Record<Long, CoursePayload>> sink, String name, int holeCount) {
    assertEquals(1, sink.received().size());
    var record = sink.received().get(0).getPayload();
    var payload = record.value();

    assertEquals(1L, record.key());
    assertEquals(name, payload.getName());
    assertEquals(holeCount, payload.getHoles().size());
  }

  private static CourseEntity courseMock() {
    return new CourseEntity(1L, "DG Course", List.of());
  }

}
