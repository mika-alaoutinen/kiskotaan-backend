package mikaa.feature.course;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.memory.InMemoryConnector;
import io.smallrye.reactive.messaging.memory.InMemorySource;
import jakarta.enterprise.inject.Any;
import jakarta.inject.Inject;
import mikaa.CoursePayload;
import mikaa.CourseUpdated;
import mikaa.config.IncomingChannels;

@QuarkusTest
class CourseConsumerTest {

  static final Uni<CourseEntity> LAAJIS_UNI = Uni.createFrom().item(new CourseEntity(1l, "Laajis", List.of()));

  @InjectMock
  CourseRepository repository;

  @Any
  @Inject
  InMemoryConnector connector;

  @Test
  void handles_add_course_event() {
    InMemorySource<CoursePayload> source = connector.source(IncomingChannels.Course.COURSE_ADDED);
    source.send(new CoursePayload(1l, "New Course", List.of()));

    verify(repository, atLeastOnce()).persist(any(CourseEntity.class));
  }

  @Test
  void handles_delete_course_event() {
    when(repository.findByExternalId(anyLong())).thenReturn(LAAJIS_UNI);

    InMemorySource<CoursePayload> source = connector.source(IncomingChannels.Course.COURSE_DELETED);
    source.send(new CoursePayload(1l, "Laajis", List.of()));

    verify(repository, atLeastOnce()).delete(any(CourseEntity.class));
  }

  @Test
  void does_nothing_on_delete_if_course_not_found() {
    InMemorySource<CoursePayload> source = connector.source(IncomingChannels.Course.COURSE_DELETED);
    source.send(new CoursePayload(1l, "Laajis", List.of()));

    verify(repository, never()).delete(any(CourseEntity.class));
  }

  @Test
  void handles_update_course_event() {
    when(repository.findByExternalId(anyLong())).thenReturn(LAAJIS_UNI);

    InMemorySource<CourseUpdated> source = connector.source(IncomingChannels.Course.COURSE_UPDATED);
    source.send(new CourseUpdated(1l, "Laajis v2"));

    verify(repository, atLeastOnce()).update(any(CourseEntity.class));
  }

  @Test
  void does_nothing_on_update_if_course_not_found() {
    InMemorySource<CourseUpdated> source = connector.source(IncomingChannels.Course.COURSE_UPDATED);
    source.send(new CourseUpdated(1l, "Laajis v2"));

    verify(repository, never()).update(any(CourseEntity.class));
  }

}
