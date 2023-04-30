package mikaa.producers.courses;

import mikaa.CoursePayload;
import mikaa.CourseUpdated;

public interface CourseProducer {

  void courseAdded(CoursePayload payload);

  void courseUpdated(CourseUpdated payload);

  void courseDeleted(CoursePayload payload);

}
