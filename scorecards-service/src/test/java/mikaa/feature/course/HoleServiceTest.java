package mikaa.feature.course;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.InjectMock;
import mikaa.kiskotaan.domain.HolePayload;

@QuarkusTest
class HoleServiceTest {

  static final HolePayload HOLE = new HolePayload(123L, 111L, 2, 3, 85);

  @InjectMock
  CourseRepository repository;

  HoleService service;

  @BeforeEach
  void setup() {
    service = new HoleService(repository);
  }

  @Test
  void should_add_hole_to_course() {
    when(repository.findByExternalId(anyLong())).thenReturn(Optional.of(mockCourse()));
    service.add(HOLE);
    var expectedCourse = new CourseEntity(111l, List.of(new HoleEntity(1, 3), new HoleEntity(2, 3)), "Laajis");
    verify(repository, atLeastOnce()).persist(expectedCourse);
  }

  @Test
  void should_do_nothing_on_add_if_course_not_found() {
    service.add(HOLE);
    verify(repository, never()).persist(any(CourseEntity.class));
  }

  @Test
  void should_remove_hole_from_course() {
    when(repository.findByExternalId(anyLong())).thenReturn(Optional.of(mockCourse()));
    service.delete(new HolePayload(999l, 111L, 1, 3, 85));
    verify(repository, atLeastOnce()).persist(new CourseEntity(111L, List.of(), "Laajis"));
  }

  @Test
  void should_do_nothing_on_delete_if_course_not_found() {
    service.delete(HOLE);
    verify(repository, never()).persist(any(CourseEntity.class));
  }

  private static CourseEntity mockCourse() {
    var holes = new ArrayList<HoleEntity>();
    holes.add(new HoleEntity(1, 3));
    return new CourseEntity(111L, holes, "Laajis");
  }

}
