package mikaa.consumers.course;

import mikaa.CoursePayload;
import mikaa.CourseUpdated;

public interface CourseWriter {

  void add(CoursePayload payload);

  void update(CourseUpdated payload);

  void delete(CoursePayload payload);

}
