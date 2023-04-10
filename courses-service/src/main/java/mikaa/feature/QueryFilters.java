package mikaa.feature;

import mikaa.util.NameFilter;
import mikaa.util.Range;

record QueryFilters(
    NameFilter nameFilter,
    Range<Integer> holesFilter,
    Range<Integer> parFilter) {

  boolean applyAll(CourseEntity course) {
    var holes = course.getHoles();
    var coursePar = holes.stream().mapToInt(HoleEntity::getPar).sum();
    return holesFilter.within(holes.size()) && parFilter.within(coursePar);
  }

}
