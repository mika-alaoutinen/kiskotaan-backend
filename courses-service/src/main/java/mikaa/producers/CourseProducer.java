package mikaa.producers;

import mikaa.domain.Course;

public interface CourseProducer {

  static final String COURSE_STATE = "course-state";

  void courseAdded(Course course);

  void courseUpdated(Course course);

  void courseDeleted(Course course);

}
