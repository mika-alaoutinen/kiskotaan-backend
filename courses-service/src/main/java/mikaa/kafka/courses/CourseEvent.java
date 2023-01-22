package mikaa.kafka.courses;

import mikaa.dto.CourseDTO;

public record CourseEvent(CourseEventType type, CourseDTO payload) {
}
