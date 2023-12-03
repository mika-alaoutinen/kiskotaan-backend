package mikaa.feature.courses;

import java.util.Collection;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;

@ApplicationScoped
@RequiredArgsConstructor
class CourseService {

  private final CourseStore store;

  Collection<Course> streamCourses() {
    return store.streamAll().map(Course::fromPayload).toList();
  }

  Optional<Course> findCourse(long id) {
    return store.findById(id).map(Course::fromPayload);
  }

}
