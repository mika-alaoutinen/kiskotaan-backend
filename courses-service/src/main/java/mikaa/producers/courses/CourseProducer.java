package mikaa.producers.courses;

import mikaa.kiskotaan.domain.CoursePayload;
import mikaa.kiskotaan.domain.CourseUpdated;

public interface CourseProducer {

  void courseAdded(CoursePayload payload);

  void courseUpdated(CourseUpdated payload);

  void courseDeleted(CoursePayload payload);

}
