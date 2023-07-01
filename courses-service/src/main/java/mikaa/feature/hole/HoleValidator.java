package mikaa.feature.hole;

import mikaa.errors.ValidationException;
import mikaa.feature.course.CourseEntity;
import mikaa.errors.ValidationError;

interface HoleValidator {

  static void validateUniqueHoleNumber(Integer holeNumber, CourseEntity course) {
    boolean duplicateHoleNumber = course.getHoles()
        .stream()
        .map(HoleEntity::getNumber)
        .anyMatch(holeNumber::equals);

    if (duplicateHoleNumber) {
      var error = new ValidationError("hole.number", "Duplicate hole number");
      ValidationException.maybeThrow(error);
    }
  }

}
