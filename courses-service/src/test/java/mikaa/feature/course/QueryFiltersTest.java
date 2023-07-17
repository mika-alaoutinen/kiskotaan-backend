package mikaa.feature.course;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import io.quarkus.test.junit.QuarkusTest;
import mikaa.feature.hole.HoleEntity;
import mikaa.util.RangeFilter;
import mikaa.util.StringFilter;

@QuarkusTest
class QueryFiltersTest {

  private static final CourseEntity COURSE = courseMock();

  @Test
  void shouldBeTrueWhenAllFiltersMatch() {
    var nameFilter = new StringFilter("Test");
    var holesFilter = new RangeFilter<Integer>(1, 9);
    var parFilter = new RangeFilter<Integer>(9, 15);
    var filters = new QueryFilters(nameFilter, holesFilter, parFilter);

    assertTrue(filters.applyAll(COURSE));
  }

  @ParameterizedTest
  @MethodSource("nonMatchingQueryFilters")
  void shouldBeFalseWhenSomeFilterDoesNotMatch(QueryFilters nonMatching) {
    assertFalse(nonMatching.applyAll(COURSE));
  }

  private static Stream<QueryFilters> nonMatchingQueryFilters() {
    // Filters that match some course
    var name = new StringFilter("Test");
    var holes = new RangeFilter<Integer>(1, 9);
    var par = new RangeFilter<Integer>(9, 15);

    // Filters that don't match any course
    var nameNoMatch = new StringFilter("Not found");
    var holesNoMatch = new RangeFilter<Integer>(9, 18);
    var parNoMatch = new RangeFilter<Integer>(50, 60);

    return Stream.of(
        new QueryFilters(nameNoMatch, holes, par),
        new QueryFilters(name, holesNoMatch, par),
        new QueryFilters(name, holes, parNoMatch));
  }

  private static CourseEntity courseMock() {
    var holes = List.of(
        new HoleEntity(1L, 1, 3, 80, null),
        new HoleEntity(2L, 2, 4, 100, null),
        new HoleEntity(3L, 3, 5, 150, null));

    return new CourseEntity(1L, "Test course 1", holes);
  }

}
