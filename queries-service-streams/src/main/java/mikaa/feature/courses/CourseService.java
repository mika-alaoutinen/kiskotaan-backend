package mikaa.feature.courses;

import java.util.Collection;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.domain.Course;
import mikaa.kiskotaan.course.CoursePayload;
import mikaa.kiskotaan.course.Hole;

@ApplicationScoped
@RequiredArgsConstructor
class CourseService {

  private final CourseStore store;

  Collection<Course> streamCourses() {
    return store.streamAll().map(CourseService::fromPayload).toList();
  }

  Optional<Course> findCourse(long id) {
    return store.findById(id).map(CourseService::fromPayload);
  }

  private static Course fromPayload(CoursePayload c) {
    return new Course(c.getId(), c.getName(), calculateCoursePar(c));
  }

  private static int calculateCoursePar(CoursePayload c) {
    return c.getHoles().stream().mapToInt(Hole::getPar).sum();
  }

}
