package mikaa.producers.courses;

import mikaa.domain.Course;

public interface CourseProducer {

  void courseAdded(Course course);

  void courseUpdated(Course course);

  void courseDeleted(Course course);

}
