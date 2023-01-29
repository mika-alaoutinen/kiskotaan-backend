package mikaa.feature.course;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import mikaa.events.course.CoursePayload;
import mikaa.events.course.Hole;

@QuarkusTest
class CourseServiceTest {

  static final CoursePayload LAAJIS = new CoursePayload(111L, "Laajis", List.of());

  @InjectMock
  CourseRepository repository;

  CourseService service;

  @BeforeEach
  void setup() {
    service = new CourseService(repository);
  }

  @Test
  void should_save_new_course() {
    var hole = new Hole(222, 1, 3, 85);
    var newCourse = new CoursePayload(111L, "Laajis", List.of(hole));

    service.add(newCourse);
    verify(repository, atLeastOnce()).persist(new CourseEntity(111, 1, "Laajis"));
  }

  @Test
  void should_update_course() {
    var course = new CourseEntity(111L, 24, "Kaihu");
    when(repository.findByExternalId(anyLong())).thenReturn(Optional.of(course));

    service.update(LAAJIS);
    verify(repository, atLeastOnce()).persist(new CourseEntity(111L, 24, "Laajis"));
  }

  @Test
  void should_do_nothing_on_update_if_course_not_found() {
    service.update(LAAJIS);
    verify(repository, never()).persist(any(CourseEntity.class));
  }

  @Test
  void should_delete_course() {
    service.delete(LAAJIS);
    verify(repository, atLeastOnce()).deleteById(111L);
  }

}
