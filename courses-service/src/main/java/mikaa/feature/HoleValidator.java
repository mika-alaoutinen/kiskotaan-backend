package mikaa.feature;

import mikaa.errors.ValidationException;
import mikaa.errors.ValidationError;

interface HoleValidator {

  static void validateUniqueHoleNumber(HoleEntity hole, CourseEntity course) {
    boolean duplicateHoleNumber = course.getHoles()
        .stream()
        .map(HoleEntity::getHoleNumber)
        .anyMatch(num -> num == hole.getHoleNumber());

    if (duplicateHoleNumber) {
      var error = new ValidationError("number", "Duplicate hole number");
      ValidationException.maybeThrow(error);
    }
  }

}
