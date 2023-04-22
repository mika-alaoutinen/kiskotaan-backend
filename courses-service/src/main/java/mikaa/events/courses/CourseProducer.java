package mikaa.events.courses;

public interface CourseProducer {

  void courseAdded(CoursePayload course);

  void courseUpdated(CoursePayload course);

  void courseDeleted(CoursePayload course);

}
