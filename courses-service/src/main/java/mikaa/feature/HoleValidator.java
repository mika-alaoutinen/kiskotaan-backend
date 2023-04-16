package mikaa.feature;

import mikaa.errors.ValidationException;
import mikaa.errors.ValidationError;

interface HoleValidator {

  static void validateUniqueHoleNumber(Integer holeNumber, CourseEntity course) {
    boolean duplicateHoleNumber = course.getHoles()
        .stream()
        .map(HoleEntity::getHoleNumber)
        .anyMatch(holeNumber::equals);

    if (duplicateHoleNumber) {
      var error = new ValidationError("hole.number", "Duplicate hole number");
      ValidationException.maybeThrow(error);
    }
  }

}
