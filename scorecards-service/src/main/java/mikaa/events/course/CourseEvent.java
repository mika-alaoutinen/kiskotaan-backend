package mikaa.events.course;

public record CourseEvent(CourseEventType type, CourseDTO payload) {
}
