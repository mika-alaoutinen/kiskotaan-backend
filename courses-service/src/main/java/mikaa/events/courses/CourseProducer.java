package mikaa.events.courses;

public interface CourseProducer {

  void courseAdded(CoursePayload payload);

  void courseUpdated(CoursePayload payload);

  void courseDeleted(CoursePayload payload);

}
