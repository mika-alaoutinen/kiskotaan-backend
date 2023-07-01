package mikaa.feature.course;

import java.util.Optional;
import java.util.stream.Stream;

import jakarta.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;
import mikaa.errors.ValidationError;
import mikaa.errors.ValidationException;

@ApplicationScoped
@RequiredArgsConstructor
class CourseValidator {

  private final CourseRepository repository;

  void validate(CourseEntity course) {
    ValidationError[] errors = Stream.of(validateUniqueName(course.getName()))
        .flatMap(Optional::stream)
        .toArray(ValidationError[]::new);

    ValidationException.maybeThrow(errors);
  }

  private Optional<ValidationError> validateUniqueName(String name) {
    return repository.existsByName(name)
        ? Optional.of(new ValidationError("course.name", "Course name should be unique"))
        : Optional.empty();
  }

}
