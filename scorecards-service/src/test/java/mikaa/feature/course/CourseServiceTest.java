package mikaa.feature.course;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import mikaa.kiskotaan.domain.CoursePayload;
import mikaa.kiskotaan.domain.Hole;

@QuarkusTest
class CourseServiceTest {

  @InjectMock
  CourseRepository repository;

  CourseService service;

  @BeforeEach
  void setup() {
    service = new CourseService(repository);
  }

  @Test
  void should_save_new_course() {
    var hole = new Hole(222l, 1, 3, 85);
    var newCourse = new CoursePayload(111l, "Laajis", List.of(hole));

    service.add(newCourse);
    var expectedCourse = new CourseEntity(null, 111l, List.of(new HoleEntity(1, 3)), "Laajis", Set.of());
    verify(repository, atLeastOnce()).persist(expectedCourse);
  }

  @Test
  void should_update_course() {
    var course = new CourseEntity(123l, 20l, List.of(), "Kaihu", Set.of());
    when(repository.findByExternalId(anyLong())).thenReturn(Optional.of(course));

    service.update(new CoursePayload(123l, "Kaihu v2", List.of()));
    verify(repository, atLeastOnce()).persist(new CourseEntity(123l, 20l, List.of(), "Kaihu v2", Set.of()));
  }

  @Test
  void should_do_nothing_on_update_if_course_not_found() {
    service.update(new CoursePayload(111l, "Laajis", List.of()));
    verify(repository, never()).persist(any(CourseEntity.class));
  }

  @Test
  void should_delete_course() {
    service.delete(new CoursePayload(111L, "Laajis", List.of()));
    verify(repository, atLeastOnce()).deleteByExternalId(111L);
  }

}
