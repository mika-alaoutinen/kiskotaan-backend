package mikaa.events.courses;

import mikaa.kiskotaan.domain.CoursePayload;

public interface CourseProducer {

  void courseAdded(CoursePayload payload);

  void courseUpdated(CourseUpdated payload);

  void courseDeleted(CoursePayload payload);

}
