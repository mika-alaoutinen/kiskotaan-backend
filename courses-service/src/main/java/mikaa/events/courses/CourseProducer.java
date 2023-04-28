package mikaa.events.courses;

public interface CourseProducer {

  void courseAdded(CoursePayload payload);

  void courseUpdated(CourseUpdated payload);

  void courseDeleted(CoursePayload payload);

}
