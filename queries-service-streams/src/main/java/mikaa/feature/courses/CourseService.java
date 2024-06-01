package mikaa.feature.courses;

import java.util.Collection;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.RequiredArgsConstructor;
import mikaa.domain.Course;
import mikaa.domain.Hole;
import mikaa.kiskotaan.course.CoursePayload;

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

  private static Course fromPayload(CoursePayload course) {
    var holes = course.getHoles()
        .stream()
        .map(h -> new Hole(h.getNumber(), h.getPar(), h.getDistance()))
        .toList();

    return new Course(course.getId(), course.getName(), holes);
  }

}
