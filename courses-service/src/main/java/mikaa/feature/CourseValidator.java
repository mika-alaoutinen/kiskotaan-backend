package mikaa.feature;

import javax.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;
import mikaa.errors.ValidationError;
import mikaa.errors.ValidationException;

@ApplicationScoped
@RequiredArgsConstructor
class CourseValidator {

  private final CourseRepository repository;

  void validateUniqueName(CourseEntity course) {
    if (repository.existsByName(course.getName())) {
      throw new ValidationException(new ValidationError("name", "Course name should be unique"));
    }
  }

}
