package mikaa.feature;

import mikaa.util.StringFilter;
import mikaa.util.RangeFilter;

record QueryFilters(
    StringFilter nameFilter,
    RangeFilter<Integer> holesFilter,
    RangeFilter<Integer> parFilter) {

  boolean applyAll(CourseEntity course) {
    var holes = course.getHoles();
    var coursePar = holes.stream().mapToInt(HoleEntity::getPar).sum();

    return nameFilter.contains(course.getName()) &&
        holesFilter.within(holes.size()) &&
        parFilter.within(coursePar);
  }

}
