package mikaa.producers.courses;

import mikaa.kiskotaan.course.CoursePayload;

public interface CourseProducer {

  void courseAdded(CoursePayload payload);

  void courseUpdated(CoursePayload payload);

  void courseDeleted(CoursePayload payload);

}
