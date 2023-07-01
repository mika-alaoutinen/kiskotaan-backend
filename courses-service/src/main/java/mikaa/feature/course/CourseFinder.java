package mikaa.feature.course;

import java.util.Optional;

public interface CourseFinder {

  Optional<CourseEntity> findCourse(long id);

  CourseEntity findCourseOrThrow(long id);

}
