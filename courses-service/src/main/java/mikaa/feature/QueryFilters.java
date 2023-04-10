package mikaa.feature;

import mikaa.util.StringFilter;
import lombok.RequiredArgsConstructor;
import mikaa.util.RangeFilter;

@RequiredArgsConstructor
class QueryFilters {
  private final StringFilter nameFilter;
  private final RangeFilter<Integer> holesFilter;
  private final RangeFilter<Integer> parFilter;

  boolean applyAll(CourseEntity course) {
    var holes = course.getHoles();
    var coursePar = holes.stream().mapToInt(HoleEntity::getPar).sum();

    return nameFilter.contains(course.getName()) &&
        holesFilter.within(holes.size()) &&
        parFilter.within(coursePar);
  }

}
