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

  static final HolePayload HOLE = new HolePayload(123L, 111L, 1, 3, 85);

  @InjectMock
  CourseRepository repository;

  HoleService service;

  @BeforeEach
  void setup() {
    service = new HoleService(repository);
  }

  @Test
  void should_increment_hole_count_by_one() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(mockCourse()));
    service.add(HOLE);
    verify(repository, atLeastOnce()).persist(new CourseEntity(111L, 19, "Laajis"));
  }

  @Test
  void should_do_nothing_on_add_if_course_not_found() {
    service.add(HOLE);
    verify(repository, never()).persist(any(CourseEntity.class));
  }

  @Test
  void should_decrement_hole_count_by_one() {
    when(repository.findByIdOptional(anyLong())).thenReturn(Optional.of(mockCourse()));
    service.delete(HOLE);
    verify(repository, atLeastOnce()).persist(new CourseEntity(111L, 17, "Laajis"));
  }

  @Test
  void should_do_nothing_on_delete_if_course_not_found() {
    service.delete(HOLE);
    verify(repository, never()).persist(any(CourseEntity.class));
  }

  private static CourseEntity mockCourse() {
    return new CourseEntity(111L, 18, "Laajis");
  }

}
