package mikaa.kafka.courses;

public record CourseEvent(CourseEventType type, CoursePayload payload) {
}
