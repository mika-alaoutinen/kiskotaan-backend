package mikaa.producers.courses;

import mikaa.kiskotaan.courses.CoursePayload;
import mikaa.kiskotaan.courses.CourseUpdated;

public interface CourseProducer {

  void courseAdded(CoursePayload payload);

  void courseUpdated(CourseUpdated payload);

  void courseDeleted(CoursePayload payload);

}
