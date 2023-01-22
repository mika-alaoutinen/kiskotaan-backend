package mikaa.events.course;

public record CourseEvent(CourseEventType type, CoursePayload payload) {
}
