package mikaa.events;

import mikaa.dto.CourseDTO;

public interface CourseEvents {

  record CourseEvent(EventType type, CourseDTO course) {
  }

  enum EventType {
    COURSE_ADDED, COURSE_DELETED, COURSE_UPDATED
  }

  public static CourseEvent add(CourseDTO course) {
    return new CourseEvent(EventType.COURSE_ADDED, course);
  }

  public static CourseEvent update(CourseDTO course) {
    return new CourseEvent(EventType.COURSE_UPDATED, course);
  }

  public static CourseEvent delete(CourseDTO course) {
    return new CourseEvent(EventType.COURSE_DELETED, course);
  }

}
