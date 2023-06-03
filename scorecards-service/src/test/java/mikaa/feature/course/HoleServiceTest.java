package mikaa.feature.course;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import mikaa.kiskotaan.domain.HolePayload;

@QuarkusTest
class HoleServiceTest {

  static final HolePayload HOLE = new HolePayload(123L, 111L, 19, 3, 85);

  @InjectMock
  CourseRepository repository;

  HoleService service;

  @BeforeEach
  void setup() {
    service = new HoleService(repository);
  }

  @Test
  void should_add_hole_to_course() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(mockCourse()));
    service.add(HOLE);
    verify(repository, atLeastOnce()).persist(new CourseEntity(111L, 19, "Laajis", 62));
  }

  @Test
  void should_do_nothing_on_add_if_course_not_found() {
    service.add(HOLE);
    verify(repository, never()).persist(any(CourseEntity.class));
  }

  @Test
  void should_remove_hole_from_course() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(mockCourse()));
    service.delete(new HolePayload(999l, 111L, 1, 3, 85));
    verify(repository, atLeastOnce()).persist(new CourseEntity(111L, 17, "Laajis", 56));
  }

  @Test
  void should_do_nothing_on_delete_if_course_not_found() {
    service.delete(HOLE);
    verify(repository, never()).persist(any(CourseEntity.class));
  }

  private static CourseEntity mockCourse() {
    return new CourseEntity(111L, 18, "Laajis", 59);
  }

}
