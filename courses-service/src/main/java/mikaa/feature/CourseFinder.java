package mikaa.feature;

import java.util.Optional;

public interface CourseFinder {

  Optional<CourseEntity> findCourse(long id);

}
