package mikaa.feature;

import java.util.Optional;

interface CourseFinder {

  Optional<CourseEntity> findCourse(long id);

  CourseEntity findCourseOrThrow(long id);

}
