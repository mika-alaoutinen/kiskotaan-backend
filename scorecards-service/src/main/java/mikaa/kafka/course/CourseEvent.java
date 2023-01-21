package mikaa.kafka.course;

public record CourseEvent(CourseEventType type, CourseDTO payload) {
}
