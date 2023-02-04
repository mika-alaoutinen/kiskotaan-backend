package mikaa.feature;

import javax.enterprise.context.ApplicationScoped;

import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
class CourseValidator {

  private final CourseRepository repository;

  void validateUniqueName(CourseEntity course) {
    if (repository.existsByName(course.getName())) {
      System.out.println("Course name should be unique");
    }
  }

}
