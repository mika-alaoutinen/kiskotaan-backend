package mikaa.events.courses;

public interface CourseProducer {

  void courseAdded(CourseAdded payload);

  void courseUpdated(CourseUpdated payload);

  void courseDeleted(long id);

}
